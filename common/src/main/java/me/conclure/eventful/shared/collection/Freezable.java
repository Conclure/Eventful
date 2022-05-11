package me.conclure.eventful.shared.collection;

public interface Freezable {
    boolean isFrozen();

    Result freeze();

    enum Result {
        ALREADY_FROZEN,
        SUCCESSFUL
    }
}
