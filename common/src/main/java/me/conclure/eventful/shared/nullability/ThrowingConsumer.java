package me.conclure.eventful.shared.nullability;

public interface ThrowingConsumer<E> {
    void accept(E e) throws Exception;
}
