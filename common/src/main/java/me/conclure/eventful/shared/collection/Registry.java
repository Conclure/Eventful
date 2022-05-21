package me.conclure.eventful.shared.collection;

import me.conclure.eventful.shared.nullability.Nil;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public interface Registry<T,I> extends Dictionary<T,I>, IdObtainer<T,I> {

    static <T,I> Registry<T,I> create(Function<T,I> idMapper) {
        return new RegistryImpl<>(new HashMap<>(1,.9f), idMapper);
    }

    static <T,I> Registry<T,I> createThreadSafe(Function<T,I> idMapper) {
        return new RegistryImpl<>(new ConcurrentHashMap<>(1,.9f), idMapper);
    }

    void register(T object);
}
