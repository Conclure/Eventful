package me.conclure.eventful.model;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.UnmodifiableIterator;
import me.conclure.eventful.nullability.Nil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public interface Registry<T,I> extends Iterable<T> {

    static <T,I> Registry<T,I> create(Function<T,I> idMapper) {
        return new RegistryImpl<>(new HashMap<>(1,.9f), idMapper);
    }

    static <T,I> Registry<T,I> createThreadSafe(Function<T,I> idMapper) {
        return new RegistryImpl<>(new ConcurrentHashMap<>(1,.9f), idMapper);
    }

    Nil<T> get(I id);

    I getId(T object);

    void register(T object);

    boolean remove(I id);

    ImmutableCollection<? extends T> values();

    @NotNull UnmodifiableIterator<T> iterator();
}
