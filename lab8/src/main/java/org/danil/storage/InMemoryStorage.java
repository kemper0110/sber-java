package org.danil.storage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class InMemoryStorage<Key, Volume> implements CacheStorage<Key, Volume>, Serializable {
    private Map<Key, Volume> map;
    public InMemoryStorage() {
        this.map = new HashMap<>(10);
    }
    @Override
    public Volume get(Key key) {
        return map.get(key);
    }

    @Override
    public void put(Key key, Volume volume) {
        map.put(key, volume);
    }

    @Override
    public boolean containsKey(Key key) {
        return map.containsKey(key);
    }
}
