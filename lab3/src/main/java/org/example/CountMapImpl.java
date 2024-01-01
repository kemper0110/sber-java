package org.example;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CountMapImpl<K> implements CountMap<K> {
    protected Map<K, Integer> map = new HashMap<>();

    @Override
    public void add(K o) {
        map.merge(o, 1, Integer::sum);
    }

    @Override
    public int getCount(K o) {
        return map.getOrDefault(o, 0);
    }

    @Override
    public int remove(K o) {
        final var value = getCount(o);
        if(value != 0)
            if(value > 1)
                map.merge(o, -1, Integer::sum);
            else
                map.remove(o);
        return value;
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
        return Collections.unmodifiableMap(map);
    }

    @Override
    public void toMap(Map<? super K, Integer> destination) {
        destination.putAll(map);
    }
}
