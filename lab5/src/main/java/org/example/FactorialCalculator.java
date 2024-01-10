package org.example;

public class FactorialCalculator implements Calculator {
    @Override
    public int calc(int arg) {
        var result = 1;
        while(arg > 1)
            result *= arg--;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
