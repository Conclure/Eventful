package me.conclure.eventful.shared.storage.file;

import me.conclure.eventful.shared.collection.IdObtainer;
import me.conclure.eventful.shared.configuration.ConfigurationFactory;

import java.nio.file.Path;

public class FilePathResolver<T> {
    private final IdObtainer<T,?> idObtainer;
    private final Path basePath;
    private final ConfigurationFactory factory;

    public FilePathResolver(IdObtainer<T, ?> idObtainer, Path basePath, ConfigurationFactory factory) {
        this.idObtainer = idObtainer;
        this.basePath = basePath;
        this.factory = factory;
    }

    public Path resolve(T object) {
        return this.basePath.resolve(this.idObtainer.getId(object).toString()+this.factory);
    }
}
