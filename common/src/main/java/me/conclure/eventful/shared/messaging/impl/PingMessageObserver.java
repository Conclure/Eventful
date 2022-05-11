package me.conclure.eventful.shared.messaging.impl;

import me.conclure.eventful.shared.loggin.LoggerAdapter;
import me.conclure.eventful.shared.messaging.Message;
import me.conclure.eventful.shared.messaging.MessageObserver;
import me.conclure.eventful.shared.messaging.type.MessageType;

public class PingMessageObserver implements MessageObserver<String[]> {
    private final LoggerAdapter logger;

    public PingMessageObserver(LoggerAdapter logger) {
        this.logger = logger;
    }

    @Override
    public void onMessage(Message<String[]> message) {
        String signature = message.signature();
        String content = String.join(" ", message.content());
        this.logger.infof("[MS] (PONG f/%s) %s", signature, content);
    }
}
