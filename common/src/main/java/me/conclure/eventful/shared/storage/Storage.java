package me.conclure.eventful.shared.storage;

import org.spongepowered.configurate.ConfigurateException;

public interface Storage<T> {
    void save(T t) throws ConfigurateException;

    void load(T t);
}
