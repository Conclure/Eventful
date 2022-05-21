package me.conclure.eventful.shared.messaging;

import me.conclure.eventful.shared.Utility;
import me.conclure.eventful.shared.messaging.type.MessageType;

import java.util.concurrent.CompletableFuture;

public class MessageDraft<T> {
    private final T content;
    private final MessageType<T> type;

    public MessageDraft(T content, MessageType<T> type) {
        this.content = content;
        this.type = type;
    }

    public T content() {
        return this.content;
    }

    public MessageType<T> type() {
        return this.type;
    }

    public Message<T> sign(String signature) {
        return new Message<>(this,signature);
    }

    @Utility
    public Message<T> sign(MessageCenter center) {
        return this.sign(center.signature());
    }

    @Utility
    public CompletableFuture<Void> send(MessageCenter center) {
        return center.send(this);
    }
}
