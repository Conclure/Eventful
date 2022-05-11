package me.conclure.eventful.shared.messaging.service;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

//TODO change to CompletableFuture
public interface Messenger {

    boolean isBooted();

    void bootUp();

    void terminate();

    boolean isTerminated();

    CompletableFuture<Void> publish(String channel, MessageWriter writer);

    CompletableFuture<Void> subscribe(String channel, MessageReader reader);

    CompletableFuture<Void> unsubscribe(String channel, MessageReader reader);

    @FunctionalInterface
    interface MessageWriter {
        void write(DataOutput output) throws IOException;
    }

    @FunctionalInterface
    interface MessageReader {
        void read(DataInput input) throws IOException;
    }

    @FunctionalInterface
    interface ExceptionHandler {
        void handleException(Exception e);
    }
}
