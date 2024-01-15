package org.danil;

import lombok.RequiredArgsConstructor;

import java.util.Iterator;

@RequiredArgsConstructor
public class ArrayIterator<T> implements Iterator<T> {
    private final T[] array;
    int index = -1;
    @Override
    public boolean hasNext() {
        return index + 1 < array.length;
    }
    @Override
    public T next() {
        return array[++index];
    }
}
