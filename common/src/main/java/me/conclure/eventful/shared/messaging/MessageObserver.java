package me.conclure.eventful.shared.messaging;

public interface MessageObserver<T> {
    void onMessage(Message<T> message);
}
