package me.conclure.eventful.shared.messaging.type;

import me.conclure.eventful.shared.Utility;
import me.conclure.eventful.shared.messaging.Message;
import me.conclure.eventful.shared.messaging.MessageCenter;
import me.conclure.eventful.shared.messaging.MessageDraft;
import me.conclure.eventful.shared.messaging.MessageObserver;
import me.conclure.eventful.shared.messaging.stream.MessageIn;
import me.conclure.eventful.shared.messaging.stream.MessageOut;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class MessageType<T> {
    private final int id;

    public MessageType(int id) {
        this.id = id;
    }

    @Utility
    public void register(MessageCenter center, MessageObserver<T> observer) {
        center.register(this,observer);
    }

    @Utility
    public void register(MessageCenter center, MessageObserver<T> observer, Executor executor) {
        center.register(this,observer,executor);
    }

    public int id() {
        return this.id;
    }

    public MessageDraft<T> create(T content) {
        return new MessageDraft<>(content,this);
    }

    public abstract MessageDraft<T> toMessage(MessageIn in);

    public abstract void fromMessage(Message<T> message, MessageOut out);
}