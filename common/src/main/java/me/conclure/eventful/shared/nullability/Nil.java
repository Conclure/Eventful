package me.conclure.eventful.shared.nullability;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.*;
import java.util.stream.Stream;


public sealed interface Nil<T> permits PresentNil, AbsentNil {
    static <T> AbsentNil<T> absent() {
        return AbsentNil.getInstance();
    }

    static <T> PresentNil<T> present(@NotNull T value) {
        return PresentNil.nonNull(value);
    }

    static <T> Nil<T> optional(@Nullable T value) {
        return value == null ? Nil.absent() : Nil.present(value);
    }

    static <T> Nil<T> from(Optional<T> optional) {
        return Nil.optional(optional.orElse(null));
    }

    static <T> Nil<T> obtain(Callable<@Nullable T> supplier) {
        try {
            T t = supplier.call();
            return Nil.optional(t);
        } catch (Exception e) {
            SneakyThrower.sneakyThrow(e);
            throw new AssertionError("Unreachable code");
        }
    }

    AbsentNil<T> assertAbsent();

    PresentNil<T> assertPresent();

    @Nullable
    T value();

    T orValue(T fallback);

    T orGetValue(Supplier<T> supplier);

    Nil<T> or(@Nullable T elseValue);

    Nil<T> orGet(Supplier<@Nullable T> supplier);

    Nil<T> orFlatGet(Supplier<Nil<T>> supplier);

    boolean isAbsent();

    boolean isPresent();

    Nil<T> filter(Predicate<T> predicate);

    Nil<T> ifPresent(Consumer<T> consumer);

    Nil<T> ifPresentOrElse(Consumer<T> consumer, Runnable runnable);

    Nil<T> ifAbsent(Runnable runnable);

    PresentNil<T> orThrow(Throwable throwable);

    PresentNil<T> orGetAndThrow(Supplier<Throwable> supplier);

    <R> Nil<R> map(Function<T, Nil<R>> function);

    IntNil mapToInt(Function<T,IntNil> function);

    DoubleNil mapToDouble(Function<T,DoubleNil> function);

    Stream<T> stream();

    default CompletableFuture<@Nullable T> future() {
        return CompletableFuture.completedFuture(this.value());
    }
}