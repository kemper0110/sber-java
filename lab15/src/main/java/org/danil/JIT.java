package org.danil;

import java.util.HashMap;

public class JIT {

    public static void main(String[] args) {
        final var iterations = 100_000;
        final var map = new HashMap<Integer, String>();
        for(int i = 0; i < iterations; ++i) {
            map.put(i, "value " + i);
        }
    }
}