package me.conclure.eventful.model;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import me.conclure.eventful.nullability.Nilable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MappingImpl<T,I> implements Mapping<T,I> {
    private final Map<I,T> map;

    public MappingImpl(Map<I, T> map) {
        this.map = map;
    }

    @Override
    public Nilable<T> get(I id) {
        return Nilable.optional(this.map.get(id));
    }

    @Override
    public void set(I id, T object) {
        this.map.put(id,object);
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
