package me.conclure.eventful.proxy;

public class Event {
    public static class StartResult {
        public static final StartResult ALREADY_RUNNING = new StartResult();
        public static final StartResult INTERNAL_ERROR = new StartResult();
    }
}
