package me.conclure.eventful.shared.configuration;

import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public interface ConfigurationFactory {
    ConfigurationFactory GSON = path -> GsonConfigurationLoader.builder()
            .sink(() -> Files.newBufferedWriter(path, StandardCharsets.UTF_8))
            .source(() -> Files.newBufferedReader(path,StandardCharsets.UTF_8))
            .build();

    ConfigurationLoader<? extends ConfigurationNode> create(Path path);

    default ConfigurationNode createNode() {
        return BasicConfigurationNode.root();
    }
}
