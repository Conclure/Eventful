package me.conclure.eventful.shared.nullability;

import java.util.Objects;
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

public sealed interface PresentDoubleNil extends DoubleNil permits PresentDoubleNil.Impl {
    final class Impl implements PresentDoubleNil {
        private final double value;

        private Impl(double value) {
            Objects.requireNonNull(value);
            this.value = value;
        }

        @Override
        public double value() {
            return this.value;
        }
    }

    static PresentDoubleNil nonNull(double value) {
        return new PresentDoubleNil.Impl(value);
    }

    @Override
    default AbsentDoubleNil assertAbsent() {
        throw new AssertionError();
    }

    @Override
    default PresentDoubleNil assertPresent() {
        return this;
    }

    double value();

    @Override
    default double orValue(double fallback) {
        return this.value();
    }

    @Override
    default double orGetValue(DoubleSupplier supplier) {
        return this.value();
    }

    @Override
    default PresentDoubleNil or(double elseValue) {
        return this;
    }

    @Override
    default PresentDoubleNil orGet(DoubleSupplier supplier) {
        return this;
    }

    @Override
    default PresentDoubleNil orFlatGet(Supplier<DoubleNil> supplier) {
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
    default DoubleNil filter(DoublePredicate predicate) {
        if (predicate.test(this.value())) {
            return this;
        }
        return DoubleNil.absent();
    }

    @Override
    default PresentDoubleNil ifPresent(DoubleConsumer consumer) {
        consumer.accept(this.value());
        return this;
    }

    @Override
    default PresentDoubleNil ifPresentOrElse(DoubleConsumer consumer, Runnable runnable) {
        consumer.accept(this.value());
        return this;
    }

    @Override
    default PresentDoubleNil ifAbsent(Runnable runnable) {
        return this;
    }

    @Override
    default PresentDoubleNil orThrow(Throwable throwable) {
        return this;
    }

    @Override
    default PresentDoubleNil orGetAndThrow(Supplier<Throwable> supplier) {
        return this;
    }

    @Override
    default <R> Nil<R> map(DoubleFunction<Nil<R>> function) {
        return function.apply(this.value());
    }

    @Override
    default DoubleNil mapToDouble(DoubleFunction<DoubleNil> function) {
        return function.apply(this.value());
    }

    @Override
    default IntNil mapToInt(DoubleFunction<IntNil> function) {
        return function.apply(this.value());
    }

    @Override
    default DoubleStream stream() {
        return DoubleStream.builder().add(this.value()).build();
    }

    @Override
    default Nil<Double> boxed() {
        return this.map(Nil::present);
    }
}
