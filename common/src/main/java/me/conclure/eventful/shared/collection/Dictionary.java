package me.conclure.eventful.shared.collection;

import me.conclure.eventful.shared.nullability.Nil;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Iterator;

public interface Dictionary<T,I> extends Iterable<T> {

    Nil<T> get(I id);

    boolean remove(I id);

    @UnmodifiableView
    Collection<? extends T> values();

    @UnmodifiableView
    @Override
    Iterator<T> iterator();
}
