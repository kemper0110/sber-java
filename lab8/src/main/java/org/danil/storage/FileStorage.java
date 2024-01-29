package org.danil.storage;

import java.io.*;

public class FileStorage<Key, Volume> implements CacheStorage<Key, Volume> {
    InMemoryStorage<Key, Volume> storage;
    final String filename;

    public FileStorage(String filename) {
        this.filename = filename;
        try (var fileInputStream = new FileInputStream(filename);
             var objectInputStream = new ObjectInputStream(fileInputStream)) {
            this.storage = (InMemoryStorage<Key, Volume>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            this.storage = new InMemoryStorage<>();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Volume get(Key key) {
        return storage.get(key);
    }

    @Override
    public void put(Key key, Volume volume) {
        storage.put(key, volume);
        try (var fileOutputStream = new FileOutputStream(filename);
             var objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(storage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean containsKey(Key key) {
        return storage.containsKey(key);
    }
}
