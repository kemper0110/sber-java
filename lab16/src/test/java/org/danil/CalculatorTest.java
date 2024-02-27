package org.danil;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {
    final Calculator calculator = new Calculator();
    final List<Integer> expected10 = List.of(1, 1, 2, 3, 5, 8, 13, 21, 34, 55);

    @ParameterizedTest
    @ValueSource(ints = {
            0, 1, 2, 3, 4, 5, 10, 12, 15, 20, 30, 40
    })
    void testLength(int size) {
        final var resultSize = calculator.fibonacci(size).size();
        assertEquals(size, resultSize);
    }

    @ParameterizedTest
    @MethodSource("testFirst10NumbersProvider")
    void testFirst10Numbers(int size) {
        final var expected = expected10.subList(0, size);
        final var tenFib = calculator.fibonacci(size);
        assertEquals(expected, tenFib);
    }

    private static IntStream testFirst10NumbersProvider() {
        return IntStream.range(0, 10);
    }
}