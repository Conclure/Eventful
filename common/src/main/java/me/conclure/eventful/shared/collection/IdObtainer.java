package me.conclure.eventful.shared.collection;

public interface IdObtainer<T,I> {
    I getId(T t);
}
