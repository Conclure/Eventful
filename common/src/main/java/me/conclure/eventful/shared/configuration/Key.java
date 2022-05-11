package me.conclure.eventful.shared.configuration;

import me.conclure.eventful.shared.loggin.LoggerAdapter;
import me.conclure.eventful.shared.nullability.Nil;
import me.conclure.eventful.shared.nullability.ThrowingConsumer;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class Key<T> {
    private final Function<Key.Context,T> mapper;
    private final List<Dependency<Object>> dependencies;
    private final Nil<ThrowingConsumer<ConfigurationNode>> populator;

    Key(Function<Context, T> mapper, List<Dependency<Object>> dependencies, Nil<ThrowingConsumer<ConfigurationNode>> populator) {
        this.mapper = mapper;
        this.dependencies = List.copyOf(dependencies);
        this.populator = populator;
    }

    List<Dependency<Object>> dependencies() {
        return this.dependencies;
    }

    Function<Context, T> mapper() {
        return this.mapper;
    }

    Nil<ThrowingConsumer<ConfigurationNode>> populator() {
        return this.populator;
    }

    T load(Key.Context context) {
        return this.mapper.apply(context);
    }

    void populate(ConfigurationNode node) {
        this.populator.ifPresent(thePopulator -> {
            try {
                thePopulator.accept(node);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    static String missingValueMessage(String... path) {
        return "No value was found at %s".formatted(String.join(".",path));
    }

    static String badValueMessage(String... path) {
        return "Unexpected value was found at %s".formatted(String.join(".",path));
    }

    record Dependency<T>(Key<T> key, BiPredicate<Key<T>,T> predicate){}

    static final class Context {
        final ConfigurationNode node;
        final LoggerAdapter logger;

        Context(ConfigurationNode node, LoggerAdapter logger) {
            this.node = node;
            this.logger = logger;
        }

        ConfigurationNode node() {
            return this.node;
        }

        LoggerAdapter logger() {
            return this.logger;
        }

        void report(String message) {
            throw new ConfigurationException(message);
        }

        void reportMissingValue(String... path) {
            this.report(Key.missingValueMessage(path));
        }

        void reportBadValue(String... path) {
            this.report(Key.badValueMessage(path));
        }

        void warn(String message) {
            this.logger().warn(message);
        }

        void warnMissingValue(String... path) {
            this.warn(Key.missingValueMessage(path));
        }

        void warnBadValue(String... path) {
            this.warn(Key.badValueMessage(path));
        }
    }
}
