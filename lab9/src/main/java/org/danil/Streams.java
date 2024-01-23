package org.danil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class Streams<T> {
    List<T> list;
    Streams(List<T> list) {
        this.list = list;
    }
    public static <T> Streams<T> of(List<T> list) {
        return new Streams<>(new ArrayList<>(list));
    }

    public Streams<T> filter(Predicate<T> predicate) {
        final var newlist = new ArrayList<T>(this.list.size());

        for (T t : this.list)
            if(predicate.test(t))
                newlist.add(t);

        this.list = newlist;
        return this;
    }

    public <V> Streams<V> transform(Function<T, V> mapper) {
        final var newlist = new ArrayList<V>(this.list.size());

        for (T t : this.list)
            newlist.add(mapper.apply(t));

        return new Streams<>(newlist);
    }

    public <K, V> Map<K, V> toMap(Function<T, K> keyMapper, Function<T, V> valueMapper) {
        final var map = new HashMap<K, V>(this.list.size());

        for (T t : list)
            map.put(keyMapper.apply(t), valueMapper.apply(t));

        return map;
    }
}

