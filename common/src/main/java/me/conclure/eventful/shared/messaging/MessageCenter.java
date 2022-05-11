package me.conclure.eventful.shared.messaging;

import me.conclure.eventful.shared.Identifier;
import me.conclure.eventful.shared.collection.Registry;
import me.conclure.eventful.shared.config.MessagingInfo;
import me.conclure.eventful.shared.messaging.service.Messenger;
import me.conclure.eventful.shared.messaging.stream.MessageIn;
import me.conclure.eventful.shared.messaging.stream.MessageOut;
import me.conclure.eventful.shared.messaging.type.MessageType;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class MessageCenter {
    private final Messenger messenger;
    private final Registry<MessageType<?>,Integer> registry;
    private final Map<MessageType<?>, List<MessageObserver<?>>> listenerMap;
    private final String signature;
    private final Identifier channel;

    record ExecutionForwardingMessageObserver<T>(MessageObserver<T> observer, Executor executor) implements MessageObserver<T> {
        @Override
        public void onMessage(Message<T> message) {
            this.executor.execute(() -> {
                this.observer.onMessage(message);
            });
        }
    }

    public MessageCenter(Messenger messenger, Registry<MessageType<?>, Integer> registry, String signature, Identifier channel) {
        this.messenger = messenger;
        this.registry = registry;
        this.signature = signature;
        this.channel = channel;
        this.listenerMap = new IdentityHashMap<>();
    }

    public String signature() {
        return this.signature;
    }

    public Identifier channel() {
        return this.channel;
    }

    public static Builder builder() {
        return new Builder();
    }

    public <T> CompletableFuture<Void> send(Message<T> message) {
        MessageType<T> messageType = message.type();
        return this.messenger.publish(this.channel.toString(), out -> {
            out.writeUTF(this.signature);
            out.writeInt(messageType.id());
            messageType.fromMessage(message,new MessageOut(out));
        });
    }

    public <T> void register(MessageType<T> messageType, MessageObserver<T> observer) {
        List<MessageObserver<?>> observers = this.listenerMap.computeIfAbsent(messageType, key -> {
            return new ArrayList<>(1);
        });
        ((ArrayList<MessageObserver<?>>)observers).ensureCapacity(observers.size()+1);
        observers.add(observer);
    }

    public <T> void register(MessageType<T> messageType, MessageObserver<T> observer, Executor executor) {
        this.register(messageType,new ExecutionForwardingMessageObserver<>(observer,executor));
    }

    private void ensureMessengerIsBooted() {
        if (!this.messenger.isBooted()) {
            throw new IllegalStateException("Messenger is not booted");
        }
    }

    public void bootUp() {
        this.ensureMessengerIsBooted();
        this.messenger.subscribe(this.channel.toString(), in -> {
            String senderSignature = in.readUTF();
            int messageId = in.readInt();

            boolean theSenderIsTheReceiver = senderSignature.equals(this.signature);
            if (theSenderIsTheReceiver) {
                return;
            }

            MessageType<?> messageType = this.registry.get(messageId)
                    .orGetAndThrow(() -> new RuntimeException("Invalid message id"))
                    .value();
            Message message = messageType.toMessage(new MessageIn(in)).build(senderSignature);
            List<MessageObserver<?>> observers = this.listenerMap.getOrDefault(messageType, Collections.emptyList());

            for (MessageObserver<?> observer : observers) {
                observer.onMessage(message);
            }
        }).join();
    }

    public static class Builder {
        private Messenger messenger;
        private Registry<MessageType<?>, Integer> registry;
        private String signature;
        private Identifier channel;

        public Builder channel(Identifier channel) {
            this.channel = channel;
            return this;
        }

        public Builder messenger(Messenger messenger) {
            this.messenger = messenger;
            return this;
        }

        public Builder signature(String signature) {
            this.signature = signature;
            return this;
        }

        public Builder fromMessagingInfo(MessagingInfo messagingInfo) {
            return this.signature(messagingInfo.signature()).channel(messagingInfo.channel());
        }

        public Builder registry(Registry<MessageType<?>, Integer> registry) {
            this.registry = registry;
            return this;
        }

        public MessageCenter build() {
            return new MessageCenter(this.messenger,this.registry,this.signature,this.channel);
        }
    }
}