package me.conclure.eventful.nullability;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public sealed interface Nilable<T> permits PresentNilable, AbsentNilable {
    static <T> AbsentNilable<T> absent() {
        return AbsentNilable.getInstance();
    }

    static <T> PresentNilable<T> present(T value) {
        return PresentNilable.nonNull(value);
    }

    static <T> Nilable<T> optional(T value) {
        if (value == null) {
            return Nilable.absent();
        }
        return Nilable.present(value);
    }

    AbsentNilable<T> assertAbsent();

    PresentNilable<T> assertPresent();

    @Nullable
    T value();

    T orValue(T fallback);

    T orGetValue(Supplier<T> supplier);

    Nilable<T> or(@Nullable T elseValue);

    Nilable<T> orGet(Supplier<@Nullable T> supplier);

    boolean isAbsent();

    boolean isPresent();

    Nilable<T> filter(Predicate<T> predicate);

    Nilable<T> ifPresent(Consumer<T> consumer);

    Nilable<T> ifPresentOrElse(Consumer<T> consumer, Runnable runnable);

    Nilable<T> ifAbsent(Runnable runnable);

    PresentNilable<T> orThrow(Throwable throwable);

    PresentNilable<T> orGetAndThrow(Supplier<Throwable> supplier);

    <R> Nilable<R> map(Function<T, @Nullable R> function);

    <R> Nilable<R> flatMap(Function<T, Nilable<R>> function);

    Stream<T> stream();

    default CompletableFuture<T> future() {
        return CompletableFuture.completedFuture(this.value());
    }
}