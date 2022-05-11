package me.conclure.eventful.shared.configuration;

import me.conclure.eventful.shared.loggin.LoggerAdapter;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.function.Function;

public interface KeyFactory<T> {

    static <T> KeyBuilder<T> builder() {
        return new KeyBuilder<>();
    }

    T load(KeyFactory.Facade<T> facade) throws Exception;

    final class Facade<T> {
        private final Key.Context keyContext;
        private final KeyFactory.Context<T> factoryContext;

        public Facade(Key.Context keyContext, Context<T> factoryContext) {
            this.keyContext = keyContext;
            this.factoryContext = factoryContext;
        }

        Key.Context keyContext() {
            return this.keyContext;
        }

        KeyFactory.Context<T> factoryContext() {
            return this.factoryContext;
        }

        public String[] path() {
            return this.factoryContext.path();
        }

        public ConfigurationNode node() {
            return this.keyContext.node();
        }

        public LoggerAdapter logger() {
            return this.keyContext.logger();
        }

        public void report(String message) {
            this.keyContext.report(message);
        }

        public void reportMissingValue(String... subPath) {
            String[] superPath = this.path();
            if (subPath.length == 0) {
                this.keyContext.reportMissingValue(superPath);
                return;
            }
            String[] path = new String[subPath.length+superPath.length];
            System.arraycopy(superPath, 0, path, 0, superPath.length);
            System.arraycopy(subPath, 0, path, superPath.length, subPath.length);

            this.keyContext.reportMissingValue(path);
        }

        public void reportBadValue(String... subPath) {
            String[] superPath = this.path();
            if (subPath.length == 0) {
                this.keyContext.reportBadValue(superPath);
                return;
            }
            String[] path = new String[subPath.length+superPath.length];
            System.arraycopy(superPath, 0, path, 0, superPath.length);
            System.arraycopy(subPath, 0, path, superPath.length, subPath.length);

            this.keyContext.reportBadValue(path);
        }

        public void warn(String message) {
            this.keyContext.warn(message);
        }

        public void warnMissingValue() {
            this.keyContext.warnMissingValue(this.path());
        }

        public void warnBadValue() {
            this.keyContext.warnBadValue(this.path());
        }
    }

    final class Context<T> implements Function<Key.Context, T> {
        final KeyFactory<T> factory;
        final String[] path;

        Context(KeyFactory<T> factory, String... path) {
            this.factory = factory;
            this.path = path;
        }

        @Override
        public T apply(Key.Context context) {
            try {
                return this.factory.load(new Facade<>(context, this));
            } catch (Exception e) {
                throw new ConfigurationException(e);
            }
        }

        String[] path() {
            return this.path;
        }
    }
}
