package org.danil.storage;

import java.io.*;

public class PersistentStorage<Key, Volume> implements CacheStorage<Key, Volume> {
    InMemoryStorage<Key, Volume> storage;
    String filename;
    boolean zip;

    public PersistentStorage(String filename, boolean zip) {
        this.filename = filename;
        this.zip = zip;

        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fis)) {
            this.storage = (InMemoryStorage<Key, Volume>) in.readObject();
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
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fos)) {
            out.writeObject(storage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean containsKey(Key key) {
        return storage.containsKey(key);
    }
}
