package me.conclure.eventful.shared.messaging;

import me.conclure.eventful.shared.Utility;
import me.conclure.eventful.shared.messaging.type.MessageType;

import java.util.Objects;

public class Message<T> {
    private final MessageType<T> type;
    private final T rawValue;
    private final String senderSignature;

    public Message(MessageType<T> type, T rawValue, String senderSignature) {
        this.type = type;
        this.rawValue = rawValue;
        this.senderSignature = senderSignature;
    }

    public static <T> Builder<T> builder(MessageType<T> type) {
        return new Builder<>(type);
    }

    public String senderSignature() {
        return this.senderSignature;
    }

    @Utility
    public void send(MessageCenter center) {
        center.send(this);
    }

    public MessageType<T> type() {
        return this.type;
    }

    public T unwrap() {
        return this.rawValue;
    }

    public static class Builder<T> {
        private final MessageType<T> type;
        private T content;

        public Builder(MessageType<T> type) {
            this.type = type;
        }

        public Builder<T> content(T content) {
            this.content = content;
            return this;
        }

        public Message<T> build(String signature) {
            Objects.requireNonNull(this.type);
            Objects.requireNonNull(this.content);
            Objects.requireNonNull(signature);
            return new Message<>(this.type,this.content,signature);
        }
    }
}
