package me.conclure.eventful.model;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.UnmodifiableIterator;
import me.conclure.eventful.nullability.Nilable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public interface Mapping<T,I> extends Iterable<T> {

    static <T,I> Mapping<T,I> create() {
        return new MappingImpl<>(new HashMap<>(1,.9f));
    }

    static <T,I> Mapping<T,I> createThreadSafe() {
        return new MappingImpl<>(new ConcurrentHashMap<>(1,.9f));
    }

    Nilable<T> get(I id);

    void set(I id, T object);

    boolean remove(I id);

    ImmutableCollection<? extends T> values();

    @NotNull UnmodifiableIterator<T> iterator();
}
