package me.conclure.eventful.shared.collection;

import me.conclure.eventful.shared.nullability.Nil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
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

    @UnmodifiableView
    @Override
    public Collection<? extends T> values() {
        return Collections.unmodifiableCollection(this.map.values());
    }

    @UnmodifiableView
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            final Iterator<? extends T> it = RepositoryImpl.this.values().iterator();
            @Override
            public boolean hasNext() {
                return this.it.hasNext();
            }

            @Override
            public T next() {
                return this.it.next();
            }
        };
    }
}
