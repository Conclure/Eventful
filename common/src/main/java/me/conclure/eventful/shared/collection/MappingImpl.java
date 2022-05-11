package me.conclure.eventful.shared.collection;

import me.conclure.eventful.shared.nullability.Nil;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class MappingImpl<T,I> implements Mapping<T,I> {
    private final Map<I,T> map;

    public MappingImpl(Map<I, T> map) {
        this.map = map;
    }

    public static <T,I> Mapping<T,I> createFreezable(Map<I, T> map) {
        return new FreezableMappingImpl<>(new MappingImpl<>(map));
    }

    @Override
    public Nil<T> get(I id) {
        return Nil.optional(this.map.get(id));
    }

    @Override
    public void set(I id, T object) {
        this.map.put(id,object);
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
        return new Iterator<T>() {
            final Iterator<? extends T> it = MappingImpl.this.values().iterator();
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

    static class FreezableMappingImpl<T,I> implements Freezable, Mapping<T,I> {
        private boolean isFrozen;
        private final MappingImpl<T,I> mappingDelegate;


        FreezableMappingImpl(MappingImpl<T, I> mappingDelegate) {
            this.mappingDelegate = mappingDelegate;
        }

        @Override
        public boolean isFrozen() {
            return this.isFrozen;
        }

        @Override
        public Result freeze() {
            if (this.isFrozen) {
                return Result.ALREADY_FROZEN;
            }
            this.isFrozen = true;
            return Result.SUCCESSFUL;
        }

        @Override
        public Nil<T> get(I id) {
            return this.mappingDelegate.get(id);
        }

        @Override
        public void set(I id, T object) {
            if (this.isFrozen) {
                throw new IllegalStateException("Is frozen");
            }
            this.mappingDelegate.set(id,object);
        }

        @Override
        public boolean remove(I id) {
            if (this.isFrozen) {
                throw new IllegalStateException("Is frozen");
            }
            return this.mappingDelegate.remove(id);
        }

        @Override
        public @UnmodifiableView Collection<? extends T> values() {
            return this.mappingDelegate.values();
        }

        @Override
        public @UnmodifiableView Iterator<T> iterator() {
            return this.mappingDelegate.iterator();
        }
    }

    static class ThreadSafeFreezableImpl<T,I> implements Mapping<T,I>, Freezable {

        @Override
        public boolean isFrozen() {
            return false;
        }

        @Override
        public Result freeze() {
            return null;
        }

        @Override
        public Nil<T> get(I id) {
            return null;
        }

        @Override
        public void set(I id, T object) {

        }

        @Override
        public boolean remove(I id) {
            return false;
        }

        @Override
        public @UnmodifiableView Collection<? extends T> values() {
            return null;
        }

        @Override
        public @UnmodifiableView Iterator<T> iterator() {
            return null;
        }
    }
}
