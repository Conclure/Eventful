package me.conclure.eventful.model;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.UnmodifiableIterator;
import me.conclure.eventful.nullability.Nilable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public interface Repository<T,I> extends Iterable<T> {
    static <T,I> Repository<T,I> create(Function<I, T> factory, Function<T, I> idMapper) {
        return new RepositoryImpl<>(new HashMap<>(1,.9f),factory,idMapper);
    }

    static <T,I> Repository<T,I> createThreadSafe(Function<I, T> factory, Function<T, I> idMapper) {
        return new RepositoryImpl<>(new ConcurrentHashMap<>(1,.9f),factory,idMapper);
    }

    T getOrCreate(I id);

    I getId(T object);

    Nilable<T> getIfPresent(I id);

    boolean remove(I id);

    ImmutableCollection<? extends T> values();

    @NotNull UnmodifiableIterator<T> iterator();
}
