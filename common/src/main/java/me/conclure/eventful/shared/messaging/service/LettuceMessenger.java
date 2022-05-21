package me.conclure.eventful.shared.messaging.service;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import me.conclure.eventful.shared.config.RedisInfo;
import me.conclure.eventful.shared.nullability.Nil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LettuceMessenger implements Messenger {
    static final ExceptionHandler defaultHandler = Throwable::printStackTrace;

    private final Map<String, Set<MessageReader>> callbackMap;
    private final RedisClient redisClient;
    private final ExceptionHandler exceptionHandler;
    private Nil<StatefulRedisPubSubConnection<String,String>> writeConnection = Nil.absent();
    private Nil<StatefulRedisPubSubConnection<String,String>> readConnection = Nil.absent();
    private Nil<Lock> lock = Nil.absent();
    private State state = State.VIRGIN;

    enum State {
        VIRGIN,
        BOOTED,
        TERMINATED
    }

    public LettuceMessenger(RedisClient redisClient, ExceptionHandler exceptionHandler) {
        this.redisClient = redisClient;
        this.exceptionHandler = exceptionHandler;
        this.callbackMap = new HashMap<>();
    }

    public static LettuceMessenger create(RedisInfo info, ExceptionHandler exceptionHandler) {
        RedisURI uri = RedisURI.create(info.address(),info.port());
        uri.setSsl(info.isSSL());
        info.password().ifPresent(uri::setPassword);
        info.username().ifPresent(uri::setUsername);

        return new LettuceMessenger(RedisClient.create(uri), exceptionHandler);
    }

    public static LettuceMessenger create(RedisInfo info) {
        return create(info,defaultHandler);
    }

    private void bootUpConnections() {
        this.writeConnection = Nil.present(this.redisClient.connectPubSub());
        this.readConnection = Nil.present(this.redisClient.connectPubSub());
        this.readConnection.assertPresent().value().addListener(new IncomingMessageHandler());
    }

    @Override
    public boolean isBooted() {
        return this.state == State.BOOTED;
    }

    private void ensureVirginState() {
        if (this.state != State.VIRGIN) {
            String message = "Expected virgin state, found %s".formatted(this.state.name().toLowerCase(Locale.ROOT));
            throw new IllegalStateException(message);
        }
    }

    @Override
    public void bootUp() {
        this.ensureVirginState();
        this.lock = Nil.present(new ReentrantLock(true));
        Lock lock = this.lock.assertPresent().value();
        if (lock.tryLock()) {
            try {
                this.bootUpConnections();
                this.state = State.BOOTED;
            } finally {
                lock.unlock();
            }
        } else {
            throw new IllegalStateException("Lock was stolen before completing boot up");
        }
    }

    private Nil<RuntimeException> ensureBootedState() {
        if (this.state != State.BOOTED) {
            String message = "Expected booted state, found %s".formatted(this.state.name().toLowerCase(Locale.ROOT));
            IllegalStateException exception = new IllegalStateException(message);
            return Nil.present(exception);
        }
        return Nil.absent();
    }

    private Nil<CompletableFuture<Void>> ensureBootedStageToFuture() {
        return this.ensureBootedState()
                .map(ex -> Nil.present(CompletableFuture.failedFuture(ex)));
    }

    private void closeConnections() {
        this.readConnection.assertPresent().value().close();
        this.writeConnection.assertPresent().value().close();
    }

    private void finalizeTermination() {
        this.state = State.TERMINATED;
    }

    private void ensureBootedStateForTermination() {
        this.ensureBootedState().ifPresent(exc -> {
            throw exc;
        });
    }

    @Override
    public void terminate() {
        this.ensureBootedStateForTermination();
        Lock lock = this.lock.assertPresent().value();
        lock.lock();
        try {
            this.closeConnections();
        } finally {
            this.finalizeTermination();
            lock.unlock();
            this.lock = Nil.absent();
        }
    }

    @Override
    public boolean isTerminated() {
        return this.state == State.TERMINATED;
    }

    private CompletableFuture<Void> writeAndSend(String channel, MessageWriter writer) {
        return CompletableFuture.runAsync(() -> {
            try (var arrOut = new ByteArrayOutputStream();
                 var dataOut = new DataOutputStream(arrOut)) {
                writer.write(dataOut);
                this.writeConnection.assertPresent()
                        .value()
                        .async()
                        .publish(channel, arrOut.toString(StandardCharsets.UTF_8))
                        .toCompletableFuture()
                        .join();
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> publish(String channel, MessageWriter writer) {
        Nil<CompletableFuture<Void>> stateCheck = this.ensureBootedStageToFuture();
        boolean hasWrongState = stateCheck.isPresent();
        if (hasWrongState) {
            return stateCheck.assertPresent().value();
        }
        return this.writeAndSend(channel, writer);
    }

    private CompletableFuture<Void> addReaderToCallbackMap(String channel, MessageReader reader) {
        Set<MessageReader> readers = this.callbackMap.computeIfAbsent(channel, key -> {
            return new HashSet<>(1);
        });
        readers.add(reader);
        return this.readConnection.assertPresent()
                .value()
                .async()
                .subscribe(channel)
                .toCompletableFuture()
                .thenRun(() -> {});
    }

    @Override
    public CompletableFuture<Void> subscribe(String channel, MessageReader reader) {
        Nil<CompletableFuture<Void>> stateCheck = this.ensureBootedStageToFuture();
        boolean hasWrongState = stateCheck.isPresent();
        if (hasWrongState) {
            return stateCheck.assertPresent().value();
        }
        return CompletableFuture.runAsync(() -> {
            Lock lock = this.lock.assertPresent().value();
            lock.lock();
            try {
                this.addReaderToCallbackMap(channel, reader).join();
            } finally {
                lock.unlock();
            }
        });
    }

    private CompletableFuture<Void> removeReaderFromCallbackMap(String channel) {
        Set<MessageReader> readers = this.callbackMap.getOrDefault(channel, Collections.emptySet());
        readers.clear();
        return this.readConnection.assertPresent()
                .value()
                .async()
                .unsubscribe(channel)
                .toCompletableFuture()
                .thenRun(() -> {});
    }

    @Override
    public CompletableFuture<Void> unsubscribe(String channel, MessageReader reader) {
        Nil<CompletableFuture<Void>> stateCheck = this.ensureBootedStageToFuture();
        boolean hasWrongState = stateCheck.isPresent();
        if (hasWrongState) {
            return stateCheck.assertPresent().value();
        }
        return CompletableFuture.runAsync(() -> {
            Lock lock = this.lock.assertPresent().value();
            lock.lock();
            try {
                this.removeReaderFromCallbackMap(channel).join();
            } finally {
                lock.unlock();
            }
        });
    }

    private final class IncomingMessageHandler implements RedisPubSubListener<String, String> {

        @Override
        public void message(String channel, String message) {
            Set<MessageReader> defaultValue = Collections.emptySet();
            Set<MessageReader> readers = LettuceMessenger.this.callbackMap.getOrDefault(channel, defaultValue);
            for (MessageReader reader : readers) {
                try (var arrIn = new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8));
                     var dataIn = new DataInputStream(arrIn)) {
                    reader.read(dataIn);
                } catch (Exception e) {
                    LettuceMessenger.this.exceptionHandler.handleException(e);
                }
            }
        }

        @Override
        public void message(String pattern, String channel, String message) {
        }

        @Override
        public void subscribed(String channel, long count) {
        }

        @Override
        public void psubscribed(String pattern, long count) {
        }

        @Override
        public void unsubscribed(String channel, long count) {
        }

        @Override
        public void punsubscribed(String pattern, long count) {
        }
    }
}
