package org.example;

import org.example.reflex.Child;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class Task2Test {
    @Test
    void testMethods() {
        final var child = new Child();
        System.out.println("all methods: " + Arrays.toString(child.getClass().getMethods()));
        System.out.println("all declared methods: " + Arrays.toString(child.getClass().getDeclaredMethods()));
    }
}
