package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FactorialCalculatorTest {

    @Test
    void calc() {
        final Calculator c = new FactorialCalculator();

        assertEquals(1, c.calc(0));
        assertEquals(1, c.calc(1));
        assertEquals(2, c.calc(2));
        assertEquals(6, c.calc(3));
        assertEquals(24, c.calc(4));
        assertEquals(120, c.calc(5));
        assertEquals(120 * 6, c.calc(6));
        assertEquals(120 * 6 * 7, c.calc(7));

        assertEquals(1, c.calc(-1));
    }
}