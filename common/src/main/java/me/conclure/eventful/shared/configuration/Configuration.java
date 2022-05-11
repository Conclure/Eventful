package me.conclure.eventful.shared.configuration;

import me.conclure.eventful.shared.loggin.LoggerAdapter;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Configuration {
    private final Map<Key<?>,Value> mappedValues;
    private final List<Key<?>> keys;

    public Configuration() {
        this.mappedValues = new IdentityHashMap<>();
        this.keys = new LinkedList<>();
    }

    enum ValueState {
        IGNORE,
        LOADED
    }

    record Value(ValueState valueState, Object object) { }

    public void register(Key<?>... keys) {
        this.keys.addAll(Arrays.asList(keys));
    }

    public void register(Iterable<? extends Key<?>> keys) {
        for (Key<?> key : keys) {
            this.keys.add(key);
        }
    }

    public <T> T get(Key<T> key) {
        Object o = this.mappedValues.get(key).object();
        Objects.requireNonNull(o);

        return (T) o;
    }

    public void load(LoggerAdapter logger, ConfigurationNode node) {
        for (Key<?> key : this.keys) {
            this.loadKey(key,logger,node);
        }
    }

    private <T> Value loadKey(Key<T> key,LoggerAdapter logger, ConfigurationNode node) {
        List<Key.Dependency<Object>> dependencies = key.dependencies();
        Key.Context context = new Key.Context(node, logger);
        if (dependencies.isEmpty()) {
            T actualValue = key.load(context);
            Value value = new Value(ValueState.LOADED, actualValue);
            this.mappedValues.put(key, value);
            return value;
        }
        for (Key.Dependency<Object> dependency : dependencies) {
            Key<Object> dependencyKey = dependency.key();
            Value dependencyValue = this.mappedValues.get(dependencyKey);

            if (dependencyValue == null) {
                dependencyValue = this.loadKey(dependencyKey,logger,node);
            }

            boolean predicateFails = !dependency.predicate().test(dependencyKey, dependencyValue.object());
            boolean ignoreDependency = dependencyValue.valueState() == ValueState.IGNORE;
            if (predicateFails || ignoreDependency) {
                Value value = new Value(ValueState.IGNORE, null);
                this.mappedValues.put(key, value);
                return value;
            }
        }
        T actualValue = key.load(context);
        Value value = new Value(ValueState.LOADED, actualValue);
        this.mappedValues.put(key, value);
        return value;
    }


    public void populateDefaults(ConfigurationNode node) {
        for (Key<?> key : this.keys) {
            key.populate(node);
        }
    }
}
