package me.conclure.eventful.shared.configuration;

import me.conclure.eventful.shared.config.MessagingInfo;
import me.conclure.eventful.shared.config.RedisInfo;
import me.conclure.eventful.shared.loggin.LoggerAdapter;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DefaultConfiguration {
    private final Configuration configuration;
    private final LoggerAdapter logger;
    private final PathedConfigurationFactory factory;

    public DefaultConfiguration(LoggerAdapter logger, PathedConfigurationFactory factory) {
        this.logger = logger;
        this.factory = factory;
        this.configuration = new Configuration();
        this.registerKeys();
    }

    private void registerKeys() {
        this.configuration.register(DefaultConfigKeys.REDIS);
        this.configuration.register(DefaultConfigKeys.MESSAGING);
    }

    public void load() throws Exception {
        Path file = this.factory.path();
        Path parent = file.getParent();
        this.prepareFiles(file, parent);
        this.configuration.load(this.logger, this.factory.create().load());
    }

    private void prepareFiles(Path file, Path parent) throws IOException {
        Files.createDirectories(parent);
        if (!Files.exists(file)) {
            this.createAndSaveDefaults();
        }
    }

    private void createAndSaveDefaults() throws ConfigurateException {
        ConfigurationNode defaultNode = this.factory.createNode();
        this.configuration.populateDefaults(defaultNode);
        this.factory.create().save(defaultNode);
    }

    public RedisInfo redis() {
        return this.configuration.get(DefaultConfigKeys.REDIS);
    }

    public MessagingInfo messaging() {
        return this.configuration.get(DefaultConfigKeys.MESSAGING);
    }
}
