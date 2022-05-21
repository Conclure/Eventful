package me.conclure.eventful.shared.messaging;

import me.conclure.eventful.shared.Utility;
import me.conclure.eventful.shared.messaging.type.MessageType;

public class Message<T> {
    private final MessageDraft<T> draft;
    private final String signature;

    public Message(MessageDraft<T> draft, String signature) {
        this.draft = draft;
        this.signature = signature;
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
        return this.signature;
    }
}
