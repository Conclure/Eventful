package me.conclure.eventful.shared.nullability;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public sealed interface AbsentDoubleNil extends DoubleNil permits AbsentDoubleNil.Impl {
    final class Impl implements AbsentDoubleNil {
        private static final AbsentDoubleNil instance = new Impl();

        private static AbsentDoubleNil instance() {
            return Impl.instance;
        }
    }

    static  AbsentDoubleNil getInstance() {
        return AbsentDoubleNil.Impl.instance();
    }

    @Override
    default AbsentDoubleNil assertAbsent() {
        return this;
    }

    @Override
    default PresentDoubleNil assertPresent() {
        throw new AssertionError();
    }

    @Override
    default double orValue(double fallback) {
        return fallback;
    }

    @Override
    default double orGetValue(DoubleSupplier supplier) {
        return supplier.getAsDouble();
    }

    @Override
    default PresentDoubleNil or(double elseValue) {
        return DoubleNil.present(elseValue);
    }

    @Override
    default PresentDoubleNil orGet(DoubleSupplier supplier) {
        double elseValue = supplier.getAsDouble();
        return DoubleNil.present(elseValue);
    }

    @Override
    default DoubleNil orFlatGet(Supplier<DoubleNil> supplier) {
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
    default AbsentDoubleNil filter(DoublePredicate predicate) {
        return this;
    }

    @Override
    default AbsentDoubleNil ifPresent(DoubleConsumer consumer) {
        return this;
    }

    @Override
    default AbsentDoubleNil ifPresentOrElse(DoubleConsumer consumer, Runnable runnable) {
        runnable.run();
        return this;
    }

    @Override
    default AbsentDoubleNil ifAbsent(Runnable runnable) {
        runnable.run();
        return this;
    }

    @Override
    default PresentDoubleNil orThrow(Throwable throwable) {
        SneakyThrower.sneakyThrow(throwable);
        throw new AssertionError("Unreachable code");
    }

    @Override
    default PresentDoubleNil orGetAndThrow(Supplier<Throwable> supplier) {
        SneakyThrower.sneakyThrow(supplier.get());
        throw new AssertionError("Unreachable code");
    }

    @Override
    default <R> AbsentNil<R> map(DoubleFunction<Nil<R>> function) {
        return Nil.absent();
    }

    @Override
    default AbsentDoubleNil mapToDouble(DoubleFunction<DoubleNil> function) {
        return DoubleNil.absent();
    }

    @Override
    default IntNil mapToInt(DoubleFunction<IntNil> function) {
        return IntNil.absent();
    }

    @Override
    default DoubleStream stream() {
        return DoubleStream.builder().build();
    }

    @Override
    default Nil<Double> boxed() {
        return Nil.absent();
    }
}
