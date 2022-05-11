package me.conclure.eventful.shared.nullability;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public sealed interface PresentIntNil extends IntNil {

    final class Impl implements PresentIntNil {
        private final int value;

        private Impl(int value) {
            Objects.requireNonNull(value);
            this.value = value;
        }

        @Override
        public int value() {
            return this.value;
        }
    }

    static PresentIntNil nonNull(int value) {
        return new PresentIntNil.Impl(value);
    }

    @Override
    default AbsentIntNil assertAbsent() {
        throw new AssertionError();
    }

    @Override
    default PresentIntNil assertPresent() {
        return this;
    }

    int value();

    @Override
    default int orValue(int fallback) {
        return this.value();
    }

    @Override
    default int orGetValue(IntSupplier supplier) {
        return this.value();
    }

    @Override
    default PresentIntNil or(int elseValue) {
        return this;
    }

    @Override
    default PresentIntNil orGet(IntSupplier supplier) {
        return this;
    }

    @Override
    default PresentIntNil orFlatGet(Supplier<IntNil> supplier) {
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
    default IntNil filter(IntPredicate predicate) {
        if (predicate.test(this.value())) {
            return this;
        }
        return IntNil.absent();
    }

    @Override
    default PresentIntNil ifPresent(IntConsumer consumer) {
        consumer.accept(this.value());
        return this;
    }

    @Override
    default PresentIntNil ifPresentOrElse(IntConsumer consumer, Runnable runnable) {
        consumer.accept(this.value());
        return this;
    }

    @Override
    default PresentIntNil ifAbsent(Runnable runnable) {
        return this;
    }

    @Override
    default PresentIntNil orThrow(Throwable throwable) {
        return this;
    }

    @Override
    default PresentIntNil orGetAndThrow(Supplier<Throwable> supplier) {
        return this;
    }

    @Override
    default <R> Nil<R> map(IntFunction<Nil<R>> function) {
        return function.apply(this.value());
    }

    @Override
    default IntNil mapToInt(IntFunction<IntNil> function) {
        return function.apply(this.value());
    }

    @Override
    default DoubleNil mapToDouble(IntFunction<DoubleNil> function) {
        return function.apply(this.value());
    }

    @Override
    default IntStream stream() {
        return IntStream.builder().add(this.value()).build();
    }

    @Override
    default Nil<Integer> boxed() {
        return this.map(Nil::present);
    }
}
