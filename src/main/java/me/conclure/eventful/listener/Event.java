package me.conclure.eventful.listener;

import java.util.UUID;

public interface Event extends EventObserver {
    String id();

    Result initialize(EventContext context);

    Result start();

    boolean containsPlayer(UUID uniqueId);

    interface Result {
        Result FAIL = () -> false;
        Result SUCCESS = () -> true;

        boolean hasSucceeded();

        default boolean hasFailed() {
            return !this.hasSucceeded();
        }
    }
}