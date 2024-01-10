package org.example;

import org.example.reflex.Provider;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class Task3Test {

    @Test
    void testGetters() {
        final var p = new Provider();
        final var getters = Arrays.stream(p.getClass().getDeclaredMethods())
                .filter(m -> m.getName().startsWith("get") || m.getName().startsWith("is"))
                .toList();

        System.out.println("all getters: " + getters);
    }
}
