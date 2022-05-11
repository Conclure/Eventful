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
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LettuceMessenger implements Messenger {
    private final Map<String, Set<MessageReader>> callbackMap;
    private final RedisClient redisClient;
    private StatefulRedisPubSubConnection<String,String> writeConnection;
    private StatefulRedisPubSubConnection<String,String> readConnection;
    private Lock lock;
    private volatile State state = State.VIRGIN;

    enum State {
        VIRGIN,
        BOOTED,
        TERMINATED
    }

    public LettuceMessenger(RedisClient redisClient) {
        this.redisClient = redisClient;
        this.callbackMap = new HashMap<>();
    }

    public static LettuceMessenger create(RedisInfo info) {
        RedisURI uri = RedisURI.create(info.address(),info.port());
        uri.setSsl(info.isSSL());
        info.password().ifPresent(uri::setPassword);
        info.username().ifPresent(uri::setUsername);

        return new LettuceMessenger(RedisClient.create(uri));
    }

    private void bootUpConnections() {
        this.writeConnection = this.redisClient.connectPubSub();
        this.readConnection = this.redisClient.connectPubSub();
        this.readConnection.addListener(new IncomingMessageHandler());
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
        this.state = State.BOOTED;
        this.lock = new ReentrantLock(true);
        if (this.lock.tryLock()) {
            try {
                this.bootUpConnections();
            } finally {
                this.lock.unlock();
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
        Objects.requireNonNull(this.writeConnection);
        Objects.requireNonNull(this.readConnection);
        this.readConnection.close();
        this.writeConnection.close();
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
        Objects.requireNonNull(this.lock);
        this.lock.lock();
        try {
            this.closeConnections();
        } finally {
            this.finalizeTermination();
            this.lock.unlock();
            this.lock = null;
        }
    }

    @Override
    public boolean isTerminated() {
        return this.state == State.TERMINATED;
    }

    private CompletableFuture<Void> writeAndSend(String channel, MessageWriter writer) {
        try (var arrOut = new ByteArrayOutputStream();
             var dataOut = new DataOutputStream(arrOut)) {
            writer.write(dataOut);
            return this.writeConnection.async()
                    .publish(channel, arrOut.toString(StandardCharsets.UTF_8))
                    .toCompletableFuture()
                    .thenRun(() -> {});
        } catch (IOException e) {
            return CompletableFuture.failedFuture(e);
        }
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
        return this.readConnection.async()
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
        this.lock.lock();
        try {
            return this.addReaderToCallbackMap(channel, reader);
        } finally {
            this.lock.unlock();
        }
    }

    private CompletableFuture<Void> removeReaderFromCallbackMap(String channel) {
        Set<MessageReader> readers = this.callbackMap.getOrDefault(channel, Collections.emptySet());
        readers.clear();
        return this.readConnection.async()
                .unsubscribe(channel)
                .toCompletableFuture()
                .thenRun(() -> {
                });
    }

    @Override
    public CompletableFuture<Void> unsubscribe(String channel, MessageReader reader) {
        Nil<CompletableFuture<Void>> stateCheck = this.ensureBootedStageToFuture();
        boolean hasWrongState = stateCheck.isPresent();
        if (hasWrongState) {
            return stateCheck.assertPresent().value();
        }
        this.lock.lock();
        try {
            return this.removeReaderFromCallbackMap(channel);
        } finally {
            this.lock.unlock();
        }
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
                    new RuntimeException(e).printStackTrace();
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
