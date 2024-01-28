package org.danil.storage;

public interface CacheStorage<Key, Volume> {
    Volume get(Key key);
    void put(Key key, Volume volume);
    boolean containsKey(Key key);
}
