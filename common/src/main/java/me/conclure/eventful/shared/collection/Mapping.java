package me.conclure.eventful.shared.collection;

import me.conclure.eventful.shared.nullability.Nil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface Mapping<T,I> extends Dictionary<T,I> {

    static <T,I> Mapping<T,I> create() {
        return new MappingImpl<>(new HashMap<>(1,.9f));
    }

    static <T,I> Mapping<T,I> create(Set<Trait> traits) {
        boolean wantsFreezable = traits.contains(Trait.FREEZABLE);
        if (wantsFreezable) {
            return MappingImpl.createFreezable(new HashMap<>(1,.9f));
        }
        return new MappingImpl<>(new HashMap<>(1,.9f));
    }

    static <T,I> Mapping<T,I> create(Trait... traits) {
        return Mapping.create(Set.of(traits));
    }

    static <T,I> Mapping<T,I> createThreadSafe() {
        return new MappingImpl<>(new ConcurrentHashMap<>(1,.9f));
    }

    void set(I id, T object);
}
