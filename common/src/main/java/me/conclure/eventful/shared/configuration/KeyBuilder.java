package me.conclure.eventful.shared.configuration;

import me.conclure.eventful.shared.nullability.Nil;
import me.conclure.eventful.shared.nullability.ThrowingConsumer;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class KeyBuilder<T> {
    private String[] path = new String[0];
    private KeyFactory<T> factory;
    private ThrowingConsumer<ConfigurationNode> populator;
    private final List<Key.Dependency<Object>> dependencies = new LinkedList<>();

    public KeyBuilder<T> path(String... path) {
        this.path = path;
        return this;
    }

    public KeyBuilder<T> factory(KeyFactory<T> factory){
        this.factory = factory;
        return this;
    }

    public KeyBuilder<T> populator(ThrowingConsumer<ConfigurationNode> populator) {
        this.populator = populator;
        return this;
    }

    public <D> KeyBuilder<T> dependency(Key<D> key, BiPredicate<Key<D>,D> predicate) {
        Key.Dependency dependency = new Key.Dependency<>(key, predicate);
        this.dependencies.add(dependency);
        return this;
    }

    public Key<T> build() {
        Objects.requireNonNull(this.factory);
        Objects.requireNonNull(this.path);
        Nil<ThrowingConsumer<ConfigurationNode>> populator = Nil.optional(this.populator);
        return new Key<>(new KeyFactory.Context<>(this.factory, this.path), this.dependencies, populator);
    }
}

