package org.example;

import org.example.reflex.From;
import org.example.reflex.To;
import org.junit.jupiter.api.Test;


public class AssignTest {
    @Test
    void test() {
        final var from = new From("name", 12, 15.1f, true);
        final var to = new To();
        BeanUtils.assign(to, from);
        System.out.println(to);
    }
}
