package me.conclure.eventful.shared.collection;

import me.conclure.eventful.shared.nullability.Nil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
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

    @UnmodifiableView
    @Override
    public Collection<? extends T> values() {
        return Collections.unmodifiableCollection(this.map.values());
    }

    @UnmodifiableView
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            final Iterator<? extends T> it = RegistryImpl.this.values().iterator();

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
