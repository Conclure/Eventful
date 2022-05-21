package me.conclure.eventful.shared.storage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ObjectStreamStorage<T extends Serializable> implements Storage<T> {
    @Override
    public void save(T t) {
        try (InputStream inputStream = Files.newInputStream(pathOf((t)));
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            T readObject = (T) objectInputStream.readObject();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (ClassNotFoundException e) {
            throw new UncheckedIOException(new IOException(e));
        }
    }

    private Path pathOf(T t) {
        return null;
    }

    @Override
    public void load(T t) {

    }
}
