package me.conclure.eventful.shared.messaging;

import me.conclure.eventful.shared.Utility;
import me.conclure.eventful.shared.messaging.type.MessageType;

import java.util.concurrent.CompletableFuture;

public class Message<T> {
    private final MessageDraft<T> draft;
    private final MessageCenter center;

    public Message(MessageDraft<T> draft, MessageCenter center) {
        this.draft = draft;
        this.center = center;
    }

    @Utility
    public T content() {
        return this.draft.content();
    }

    @Utility
    public MessageType<T> type() {
        return this.draft.type();
    }

    public MessageDraft<T> draft() {
        return this.draft;
    }

    public String signature() {
        return this.center.signature();
    }
}
