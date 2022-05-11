package me.conclure.eventful.model;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import me.conclure.eventful.nullability.Nil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;

public class RegistryImpl<T,I> implements Registry<T,I> {
    private final Map<I,T> map;
    private final Function<T,I> idMapper;
    public RegistryImpl(Map<I, T> map, Function<T, I> idMapper) {
        this.map = map;
        this.idMapper = idMapper;
    }

    @Override
    public Nil<T> get(I id) {
        return Nil.optional(this.map.get(id));
    }

    @Override
    public I getId(T object) {
        return this.idMapper.apply(object);
    }

    @Override
    public void register(T object) {
        this.map.put(this.idMapper.apply(object),object);
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
