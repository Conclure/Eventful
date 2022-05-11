package me.conclure.eventful.shared.nullability;

import org.jetbrains.annotations.Nullable;

import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public sealed interface AbsentIntNil extends IntNil permits AbsentIntNil.Impl {
    final class Impl implements AbsentIntNil {
        private static final AbsentIntNil instance = new Impl();

        private static AbsentIntNil instance() {
            return Impl.instance;
        }
    }

    static  AbsentIntNil getInstance() {
        return AbsentIntNil.Impl.instance();
    }

    @Override
    default AbsentIntNil assertAbsent() {
        return this;
    }

    @Override
    default PresentIntNil assertPresent() {
        throw new AssertionError();
    }

    @Override
    default int orValue(int fallback) {
        return fallback;
    }

    @Override
    default int orGetValue(IntSupplier supplier) {
        return supplier.getAsInt();
    }

    @Override
    default PresentIntNil or(int elseValue) {
        return IntNil.present(elseValue);
    }

    @Override
    default PresentIntNil orGet(IntSupplier supplier) {
        int elseValue = supplier.getAsInt();
        return IntNil.present(elseValue);
    }

    @Override
    default IntNil orFlatGet(Supplier<IntNil> supplier) {
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
    default AbsentIntNil filter(IntPredicate predicate) {
        return this;
    }

    @Override
    default AbsentIntNil ifPresent(IntConsumer consumer) {
        return this;
    }

    @Override
    default AbsentIntNil ifPresentOrElse(IntConsumer consumer, Runnable runnable) {
        runnable.run();
        return this;
    }

    @Override
    default AbsentIntNil ifAbsent(Runnable runnable) {
        runnable.run();
        return this;
    }

    @Override
    default PresentIntNil orThrow(Throwable throwable) {
        SneakyThrower.sneakyThrow(throwable);
        throw new AssertionError("Unreachable code");
    }

    @Override
    default PresentIntNil orGetAndThrow(Supplier<Throwable> supplier) {
        SneakyThrower.sneakyThrow(supplier.get());
        throw new AssertionError("Unreachable code");
    }

    @Override
    default <R> AbsentNil<R> map(IntFunction<Nil<R>> function) {
        return Nil.absent();
    }

    @Override
    default AbsentIntNil mapToInt(IntFunction<IntNil> function) {
        return IntNil.absent();
    }

    @Override
    default DoubleNil mapToDouble(IntFunction<DoubleNil> function) {
        return DoubleNil.absent();
    }

    @Override
    default IntStream stream() {
        return IntStream.builder().build();
    }

    @Override
    default Nil<Integer> boxed() {
        return Nil.absent();
    }
}
