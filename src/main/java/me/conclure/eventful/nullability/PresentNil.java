package me.conclure.eventful.nullability;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public sealed interface PresentNil<T> extends Nil<T> permits PresentNil.Impl {

    final class Impl<T> implements PresentNil<T> {
        private final T value;

        private Impl(T value) {
            Objects.requireNonNull(value);
            this.value = value;
        }

        @Override
        public @NotNull T value() {
            return this.value;
        }
    }

    static <T> PresentNil<T> nonNull(T value) {
        return new Impl<>(value);
    }

    @Override
    default AbsentNil<T> assertAbsent() {
        throw new AssertionError();
    }

    @Override
    default PresentNil<T> assertPresent() {
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
    default PresentNil<T> or(@Nullable T elseValue) {
        return this;
    }

    @Override
    default PresentNil<T> orGet(Supplier<@Nullable T> supplier) {
        return this;
    }

    @Override
    default PresentNil<T> orFlatGet(Supplier<Nil<T>> supplier) {
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
    default Nil<T> filter(Predicate<T> predicate) {
        if (predicate.test(this.value())) {
            return this;
        }
        return Nil.absent();
    }

    @Override
    default PresentNil<T> ifPresent(Consumer<T> consumer) {
        consumer.accept(this.value());
        return this;
    }

    @Override
    default PresentNil<T> ifPresentOrElse(Consumer<T> consumer, Runnable runnable) {
        consumer.accept(this.value());
        return this;
    }

    @Override
    default PresentNil<T> ifAbsent(Runnable runnable) {
        return this;
    }

    @Override
    default PresentNil<T> orThrow(Throwable throwable) {
        return this;
    }

    @Override
    default PresentNil<T> orGetAndThrow(Supplier<Throwable> supplier) {
        return this;
    }

    @Override
    default <R> Nil<R> map(Function<T, @Nullable R> function) {
        R result = function.apply(this.value());
        if (result == null) {
            return Nil.absent();
        }
        return Nil.present(result);
    }

    @Override
    default <R> Nil<R> flatMap(Function<T, Nil<R>> function) {
        return function.apply(this.value());
    }

    @Override
    default Stream<T> stream() {
        return Stream.<T>builder().add(this.value()).build();
    }
}