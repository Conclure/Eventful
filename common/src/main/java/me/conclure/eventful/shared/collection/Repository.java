package me.conclure.eventful.shared.collection;

import me.conclure.eventful.shared.nullability.Nil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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

    Nil<T> getIfPresent(I id);

    boolean remove(I id);

    @UnmodifiableView
    Collection<? extends T> values();

    @UnmodifiableView
    @Override
    Iterator<T> iterator();
}
