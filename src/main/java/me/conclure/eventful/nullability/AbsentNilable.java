package me.conclure.eventful.nullability;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("NullableProblems")
public non-sealed interface AbsentNilable<T> extends Nilable<T> {
    AbsentNilable<?> instance = new AbsentNilable<>() {};

    static <T> AbsentNilable<T> getInstance() {
        return (AbsentNilable<T>) instance;
    }

    @Override
    default AbsentNilable<T> assertAbsent() {
        return this;
    }

    @Override
    default PresentNilable<T> assertPresent() {
        throw new AssertionError();
    }

    @Override
    @Nullable
    default T value() {
        return null;
    }

    @Override
    default T orValue(T fallback) {
        return fallback;
    }

    @Override
    default T orGetValue(Supplier<T> supplier) {
        return supplier.get();
    }

    @Override
    default Nilable<T> or(@Nullable T elseValue) {
        if (elseValue == null) {
            return this;
        }
        return Nilable.present(elseValue);
    }

    @Override
    default Nilable<T> orGet(Supplier<@Nullable T> supplier) {
        T elseValue = supplier.get();
        if (elseValue == null) {
            return this;
        }
        return Nilable.present(elseValue);
    }

    @Override
    default boolean isAbsent() {
        return true;
    }

    @Override
    default boolean isPresent() {
        return false;
    }

    @Override
    default AbsentNilable<T> filter(Predicate<T> predicate) {
        return this;
    }

    @Override
    default AbsentNilable<T> ifPresent(Consumer<T> consumer) {
        return this;
    }

    @Override
    default AbsentNilable<T> ifPresentOrElse(Consumer<T> consumer, Runnable runnable) {
        runnable.run();
        return this;
    }

    @Override
    default AbsentNilable<T> ifAbsent(Runnable runnable) {
        runnable.run();
        return this;
    }

    @Override
    default PresentNilable<T> orThrow(Throwable throwable) {
        SneakyThrower.sneakyThrow(throwable);
        throw new AssertionError("Unreachable code");
    }

    @Override
    default PresentNilable<T> orGetAndThrow(Supplier<Throwable> supplier) {
        SneakyThrower.sneakyThrow(supplier.get());
        throw new AssertionError("Unreachable code");
    }

    @Override
    default <R> AbsentNilable<R> map(Function<T, @Nullable R> function) {
        return Nilable.absent();
    }

    @Override
    default <R> AbsentNilable<R> flatMap(Function<T, Nilable<R>> function) {
        return Nilable.absent();
    }

    @Override
    default Stream<T> stream() {
        return Stream.<T>builder().build();
    }
}
