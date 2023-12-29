package org.example;

import java.util.HashMap;
import java.util.Map;

public class CountMapImpl<K> implements CountMap<K> {
    protected Map<K, Integer> map = new HashMap<>();

    @Override
    public void add(K o) {
        map.put(o, map.getOrDefault(o, 0) + 1);
    }

    @Override
    public int getCount(K o) {
        return map.getOrDefault(o, 0);
    }

    @Override
    public int remove(K o) {
        return map.remove(o);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void addAll(CountMap<? extends K> source) {
        map.putAll(source.toMap());
    }

    @Override
    public Map<K, Integer> toMap() {
        return new HashMap<>(map);
    }

    @Override
    public void toMap(Map<? super K, Integer> destination) {
        destination.putAll(map);
    }
}
