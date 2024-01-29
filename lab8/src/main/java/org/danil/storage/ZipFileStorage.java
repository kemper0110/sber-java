package org.danil.storage;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFileStorage<Key, Volume> implements CacheStorage<Key, Volume> {
    InMemoryStorage<Key, Volume> storage;
    final String filename;

    public ZipFileStorage(String filename) {
        this.filename = filename;
        try (var fileInputStream = new FileInputStream(filename);
             var zipInputStream = new ZipInputStream(fileInputStream)) {
            zipInputStream.getNextEntry();
            var objectInputStream = new ObjectInputStream(zipInputStream);
            this.storage = (InMemoryStorage<Key, Volume>) objectInputStream.readObject();
            zipInputStream.closeEntry();
        } catch (FileNotFoundException e) {
            this.storage = new InMemoryStorage<>();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void put(Key key, Volume volume) {
        storage.put(key, volume);
        try (var fileOutputStream = new FileOutputStream(filename);
             var zipOutputStream = new ZipOutputStream(fileOutputStream)) {
            zipOutputStream.putNextEntry(new ZipEntry(filename));
            var objectOutputStream = new ObjectOutputStream(zipOutputStream);
            objectOutputStream.writeObject(storage);
            zipOutputStream.closeEntry();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Volume get(Key key) {
        return storage.get(key);
    }

    @Override
    public boolean containsKey(Key key) {
        return storage.containsKey(key);
    }
}
