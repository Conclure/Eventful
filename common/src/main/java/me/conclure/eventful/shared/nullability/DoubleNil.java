package me.conclure.eventful.shared.nullability;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public sealed interface DoubleNil permits AbsentDoubleNil, PresentDoubleNil {
    static AbsentDoubleNil absent() {
        return AbsentDoubleNil.getInstance();
    }

    static PresentDoubleNil present(double value) {
        return PresentDoubleNil.nonNull(value);
    }

    static PresentDoubleNil present(@NotNull Double value) {
        return PresentDoubleNil.nonNull(value);
    }

    static DoubleNil optional(@Nullable Double value) {
        return value == null ? DoubleNil.absent() : DoubleNil.present(value);
    }

    static DoubleNil obtain(CallableDouble supplier) {
        try {
            return present(supplier.call());
        } catch (Exception e) {
            SneakyThrower.sneakyThrow(e);
            throw new AssertionError("Unreachable code");
        }
    }

    AbsentDoubleNil assertAbsent();

    PresentDoubleNil assertPresent();

    double orValue(double fallback);

    double orGetValue(DoubleSupplier supplier);

    PresentDoubleNil or(double elseValue);

    PresentDoubleNil orGet(DoubleSupplier supplier);

    DoubleNil orFlatGet(Supplier<DoubleNil> supplier);

    boolean isAbsent();

    boolean isPresent();

    DoubleNil filter(DoublePredicate predicate);

    DoubleNil ifPresent(DoubleConsumer consumer);

    DoubleNil ifPresentOrElse(DoubleConsumer consumer, Runnable runnable);

    DoubleNil ifAbsent(Runnable runnable);

    PresentDoubleNil orThrow(Throwable throwable);

    PresentDoubleNil orGetAndThrow(Supplier<Throwable> supplier);

    <R> Nil<R> map(DoubleFunction<Nil<R>> function);

    DoubleNil mapToDouble(DoubleFunction<DoubleNil> function);

    IntNil mapToInt(DoubleFunction<IntNil> function);

    DoubleStream stream();

    Nil<Double> boxed();
}
