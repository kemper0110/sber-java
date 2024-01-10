package org.example;

import org.example.reflex.Provider;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;

public class Task4Test {

    @Test
    void testFinalStrings() {
        final var p = new Provider();

        final var allNamesAreValues = Arrays.stream(p.getClass().getDeclaredFields())
                .filter(field -> field.getType() == String.class &&
                        Modifier.isPublic(field.getModifiers()) &&
                        Modifier.isStatic(field.getModifiers()) &&
                        Modifier.isFinal(field.getModifiers())
                )
//                .peek(field -> System.out.println(field.getName()))
                .allMatch(field -> {
                    try {
                        return field.get(p).equals(field.getName());
                    } catch (IllegalAccessException e) {
                        return false;
                    }
                });

        assertTrue(allNamesAreValues);
    }
}
