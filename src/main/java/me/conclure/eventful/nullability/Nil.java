package me.conclure.eventful.nullability;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;


public sealed interface Nil<T> permits PresentNil, AbsentNil {
    static <T> AbsentNil<T> absent() {
        return AbsentNil.getInstance();
    }

    static <T> PresentNil<T> present(T value) {
        return PresentNil.nonNull(value);
    }

    static <T> Nil<T> optional(T value) {
        if (value == null) {
            return Nil.absent();
        }
        return Nil.present(value);
    }

    static <T> Nil<T> obtain(Callable<@Nullable T> supplier) {
        try {
            T t = supplier.call();
            return Nil.optional(t);
        } catch (Exception e) {
            return Nil.absent();
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

    <R> Nil<R> map(Function<T, @Nullable R> function);

    <R> Nil<R> flatMap(Function<T, Nil<R>> function);

    Stream<T> stream();

    default CompletableFuture<@Nullable T> future() {
        return CompletableFuture.completedFuture(this.value());
    }
}