package me.conclure.eventful.nullability;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public non-sealed interface PresentNilable<T> extends Nilable<T> {

    static <T> PresentNilable<T> nonNull(T value) {
        Objects.requireNonNull(value);
        return () -> value;
    }

    @Override
    default AbsentNilable<T> assertAbsent() {
        throw new AssertionError();
    }

    @Override
    default PresentNilable<T> assertPresent() {
        return this;
    }

    @Override
    @NotNull
    T value();

    @Override
    default T orValue(T fallback) {
        return this.value();
    }

    @Override
    default T orGetValue(Supplier<T> supplier) {
        return this.value();
    }

    @Override
    default PresentNilable<T> or(@Nullable T elseValue) {
        return this;
    }

    @Override
    default PresentNilable<T> orGet(Supplier<@Nullable T> supplier) {
        return this;
    }

    @Override
    default boolean isAbsent() {
        return false;
    }

    @Override
    default boolean isPresent() {
        return true;
    }

    @Override
    default Nilable<T> filter(Predicate<T> predicate) {
        if (predicate.test(this.value())) {
            return this;
        }
        return Nilable.absent();
    }

    @Override
    default PresentNilable<T> ifPresent(Consumer<T> consumer) {
        consumer.accept(this.value());
        return this;
    }

    @Override
    default PresentNilable<T> ifPresentOrElse(Consumer<T> consumer, Runnable runnable) {
        consumer.accept(this.value());
        return this;
    }

    @Override
    default PresentNilable<T> ifAbsent(Runnable runnable) {
        return this;
    }

    @Override
    default PresentNilable<T> orThrow(Throwable throwable) {
        return this;
    }

    @Override
    default PresentNilable<T> orGetAndThrow(Supplier<Throwable> supplier) {
        return this;
    }

    @Override
    default <R> Nilable<R> map(Function<T, @Nullable R> function) {
        R result = function.apply(this.value());
        if (result == null) {
            return Nilable.absent();
        }
        return Nilable.present(result);
    }

    @Override
    default <R> Nilable<R> flatMap(Function<T, Nilable<R>> function) {
        return function.apply(this.value());
    }

    @Override
    default Stream<T> stream() {
        return Stream.<T>builder().add(this.value()).build();
    }
}
