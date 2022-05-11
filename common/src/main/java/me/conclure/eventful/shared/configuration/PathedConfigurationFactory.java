package me.conclure.eventful.shared.configuration;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.nio.file.Path;

public record PathedConfigurationFactory(Path path, ConfigurationFactory factory) {

    public ConfigurationLoader<? extends ConfigurationNode> create() {
        return this.factory.create(this.path);
    }

    public ConfigurationNode createNode() {
        return this.factory.createNode();
    }
}
