package me.conclure.eventful.nullability;

class SneakyThrower {
    static <T extends Throwable> void sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }
}
