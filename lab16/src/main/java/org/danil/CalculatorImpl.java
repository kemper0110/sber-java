package org.danil;

import java.util.ArrayList;
import java.util.List;

public class CalculatorImpl implements Calculator {
    public List<Integer> fibonacci(int n) {
        final var list = new ArrayList<Integer>();
        if (n == 0) return list;

        list.add(1);

        int prev = 0, next = 1, result = 0;
        for (int i = 0; i < n - 1; i++) {
            result = prev + next;
            prev = next;
            next = result;
            list.add(result);
        }

        return list;
    }
}
