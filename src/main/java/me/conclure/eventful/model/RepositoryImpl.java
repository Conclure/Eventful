package me.conclure.eventful.model;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import me.conclure.eventful.nullability.Nil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;

public class RepositoryImpl<T,I> implements Repository<T,I> {
    private final Map<I,T> map;
    private final Function<I,T> factory;
    private final Function<T,I> idMapper;

    public RepositoryImpl(Map<I, T> map, Function<I, T> factory, Function<T, I> idMapper) {
        this.map = map;
        this.factory = factory;
        this.idMapper = idMapper;
    }

    @Override
    public T getOrCreate(I id) {
        return this.map.computeIfAbsent(id, this.factory);
    }

    @Override
    public I getId(T object) {
        return this.idMapper.apply(object);
    }

    @Override
    public Nil<T> getIfPresent(I id) {
        return Nil.optional(this.map.get(id));
    }

    @Override
    public boolean remove(I id) {
        return this.map.remove(id) != null;
    }

    @Override
    public ImmutableCollection<? extends T> values() {
        return ImmutableList.copyOf(this.map.values());
    }

    @Override
    public @NotNull UnmodifiableIterator<T> iterator() {
        return Iterators.unmodifiableIterator(this.map.values().iterator());
    }
}
