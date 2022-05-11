package me.conclure.eventful.shared.nullability;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.*;
import java.util.stream.IntStream;

public sealed interface IntNil permits AbsentIntNil, PresentIntNil {
    static AbsentIntNil absent() {
        return AbsentIntNil.getInstance();
    }

    static PresentIntNil present(int value) {
        return PresentIntNil.nonNull(value);
    }

    static PresentIntNil present(@NotNull Integer value) {
        return PresentIntNil.nonNull(value);
    }

    static IntNil optional(@Nullable Integer value) {
        return value == null ? IntNil.absent() : IntNil.present(value);
    }

    static IntNil from(OptionalInt optional) {
        if (optional.isPresent()) {
            return present(optional.getAsInt());
        }
        return absent();
    }

    static IntNil obtain(CallableInt supplier) {
        try {
            return present(supplier.call());
        } catch (Exception e) {
            SneakyThrower.sneakyThrow(e);
            throw new AssertionError("Unreachable code");
        }
    }

    AbsentIntNil assertAbsent();

    PresentIntNil assertPresent();

    int orValue(int fallback);

    int orGetValue(IntSupplier supplier);

    PresentIntNil or(int elseValue);

    PresentIntNil orGet(IntSupplier supplier);

    IntNil orFlatGet(Supplier<IntNil> supplier);

    boolean isAbsent();

    boolean isPresent();

    IntNil filter(IntPredicate predicate);

    IntNil ifPresent(IntConsumer consumer);

    IntNil ifPresentOrElse(IntConsumer consumer, Runnable runnable);

    IntNil ifAbsent(Runnable runnable);

    PresentIntNil orThrow(Throwable throwable);

    PresentIntNil orGetAndThrow(Supplier<Throwable> supplier);

    <R> Nil<R> map(IntFunction<Nil<R>> function);

    DoubleNil mapToDouble(IntFunction<DoubleNil> function);

    IntNil mapToInt(IntFunction<IntNil> function);

    IntStream stream();

    Nil<Integer> boxed();
}
