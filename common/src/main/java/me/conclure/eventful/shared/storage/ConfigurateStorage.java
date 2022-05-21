package me.conclure.eventful.shared.storage;

import me.conclure.eventful.shared.collection.IdObtainer;
import me.conclure.eventful.shared.configuration.ConfigurationFactory;
import me.conclure.eventful.shared.storage.file.FilePathResolver;
import org.spongepowered.configurate.ConfigurateException;

import java.nio.file.Path;

public class ConfigurateStorage<T> implements Storage<T> {
    private final ConfigurationFactory factory;
    private final IdObtainer<T,?> idObtainer;
    private final Path basePath;
    private final FilePathResolver<T> filePathResolver;

    public ConfigurateStorage(ConfigurationFactory factory, IdObtainer<T, ?> idObtainer, Path basePath) {
        this.factory = factory;
        this.idObtainer = idObtainer;
        this.basePath = basePath;
        this.filePathResolver = new FilePathResolver<>(idObtainer,basePath,factory);
    }

    @Override
    public void save(T t) throws ConfigurateException {
        this.factory.create(this.filePathResolver.resolve(t)).save(null);
    }

    @Override
    public void load(T t) {

    }
}
