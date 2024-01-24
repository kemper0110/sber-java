package org.danil;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Spliterator.ORDERED;

public class Streams<T> {
    Spliterator<T> spliterator;

    Streams(Spliterator<T> spliterator) {
        this.spliterator = spliterator;
    }

    public static <T> Streams<T> of(Iterable<T> list) {
        return new Streams<>(list.spliterator());
    }

    public Streams<T> filter(Predicate<? super T> predicate) {
        return new Streams<>(new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, ORDERED) {
            T value = null;
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                while(spliterator.tryAdvance(value -> this.value = value)) {
                    if(!predicate.test(value)) continue;

                    action.accept(value);
                    return true;
                }
                return false;
            }
        });
    }

    public <V> Streams<V> transform(Function<? super T, ? extends V> mapper) {
        return new Streams<>(new Spliterators.AbstractSpliterator<V>(Long.MAX_VALUE, ORDERED) {
            T value = null;
            @Override
            public boolean tryAdvance(Consumer<? super V> action) {
                if(!spliterator.tryAdvance(value -> this.value = value))
                    return false;

                action.accept(mapper.apply(value));
                return true;
            }
        });
    }

    public <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        final var map = new HashMap<K, V>();

        while(spliterator.tryAdvance(t -> map.put(keyMapper.apply(t), valueMapper.apply(t)))) {

        }

        return map;
    }
}

