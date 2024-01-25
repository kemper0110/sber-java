package org.danil;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Spliterator.ORDERED;

public class Streams<T> {
    Iterator<T> iterator;

    Streams(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public static <T> Streams<T> of(Iterable<T> list) {
        return new Streams<>(list.iterator());
    }

    public Streams<T> filter(Predicate<? super T> predicate) {

        return new Streams<>(new Iterator<T>() {
            T value = null;
            @Override
            public boolean hasNext() {
                while(iterator.hasNext()) {
                    value = iterator.next();
                    if(predicate.test(value))
                        return true;
                }
                return false;
            }
            @Override
            public T next() {
                return value;
            }
        });
    }

    public <V> Streams<V> transform(Function<? super T, ? extends V> mapper) {
        return new Streams<>(new Iterator<V>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public V next() {
                return mapper.apply(iterator.next());
            }
        });
    }

    public <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        final var map = new HashMap<K, V>();

        iterator.forEachRemaining(t ->
                map.put(keyMapper.apply(t), valueMapper.apply(t))
        );

        return map;
    }
}

