package org.danil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayIteratorTest {
    String[] helloWorld;
    ArrayIterator<String> helloIterator;
    @BeforeEach
    void makeHelloWorld() {
        this.helloWorld = new String[]{"hello", "world", "this", "is", "a", "test"};
        this.helloIterator = new ArrayIterator<>(helloWorld);
    }

    @Test
    void infiniteIterate_whenNoNext() {
        int i = 0;
        for(; i < 1_000 && helloIterator.hasNext(); ++i) {}

        assertEquals(1_000, i);
    }

    @Test
    void getExpectedElement() {
        assertTrue(helloIterator.hasNext());
        assertEquals("hello", helloIterator.next());
        assertTrue(helloIterator.hasNext());
        assertEquals("world", helloIterator.next());
        assertTrue(helloIterator.hasNext());
        assertEquals("this", helloIterator.next());
        assertTrue(helloIterator.hasNext());
        assertEquals("is", helloIterator.next());
        assertTrue(helloIterator.hasNext());
        assertEquals("a", helloIterator.next());
        assertTrue(helloIterator.hasNext());
        assertEquals("test", helloIterator.next());

        assertFalse(helloIterator.hasNext());
    }
}