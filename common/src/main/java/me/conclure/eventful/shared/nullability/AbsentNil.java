package me.conclure.eventful.shared.nullability;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public sealed interface AbsentNil<T> extends Nil<T> permits AbsentNil.Impl {
    final class Impl<T> implements AbsentNil<T> {
        private static final AbsentNil<?> instance = new Impl<>();

        private static <T> AbsentNil<T> instance() {
            return (AbsentNil<T>) Impl.instance;
        }
    }

    static <T> AbsentNil<T> getInstance() {
        return Impl.instance();
    }

    @Override
    default AbsentNil<T> assertAbsent() {
        return this;
    }

    @Override
    default PresentNil<T> assertPresent() {
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
    default Nil<T> or(@Nullable T elseValue) {
        if (elseValue == null) {
            return this;
        }
        return Nil.present(elseValue);
    }

    @Override
    default Nil<T> orGet(Supplier<@Nullable T> supplier) {
        T elseValue = supplier.get();
        if (elseValue == null) {
            return this;
        }
        return Nil.present(elseValue);
    }

    @Override
    default Nil<T> orFlatGet(Supplier<Nil<T>> supplier) {
        return supplier.get();
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
    default AbsentNil<T> filter(Predicate<T> predicate) {
        return this;
    }

    @Override
    default AbsentNil<T> ifPresent(Consumer<T> consumer) {
        return this;
    }

    @Override
    default AbsentNil<T> ifPresentOrElse(Consumer<T> consumer, Runnable runnable) {
        runnable.run();
        return this;
    }

    @Override
    default AbsentNil<T> ifAbsent(Runnable runnable) {
        runnable.run();
        return this;
    }

    @Override
    default PresentNil<T> orThrow(Throwable throwable) {
        SneakyThrower.sneakyThrow(throwable);
        throw new AssertionError("Unreachable code");
    }

    @Override
    default PresentNil<T> orGetAndThrow(Supplier<Throwable> supplier) {
        SneakyThrower.sneakyThrow(supplier.get());
        throw new AssertionError("Unreachable code");
    }

    @Override
    default <R> AbsentNil<R> map(Function<T, Nil<R>> function) {
        return Nil.absent();
    }

    @Override
    default IntNil mapToInt(Function<T, IntNil> function) {
        return IntNil.absent();
    }

    @Override
    default DoubleNil mapToDouble(Function<T, DoubleNil> function) {
        return DoubleNil.absent();
    }

    @Override
    default Stream<T> stream() {
        return Stream.<T>builder().build();
    }
}
