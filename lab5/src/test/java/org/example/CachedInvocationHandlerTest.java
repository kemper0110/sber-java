package org.example;

import org.example.annotations.CachedInvocationHandler;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

class CachedInvocationHandlerTest {
    @Test
    void test() {
        Calculator delegate = new FactorialCalculator();
        Calculator calculator = (Calculator) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                delegate.getClass().getInterfaces(),
                new CachedInvocationHandler(delegate));

        System.out.println(calculator.calc(1));
        System.out.println(calculator.calc(5));
        System.out.println(calculator.calc(6));
        System.out.println(calculator.calc(1));
        System.out.println(calculator.calc(1));
        System.out.println(calculator.calc(1));
        System.out.println(calculator.calc(1));
        System.out.println(calculator.calc(1));
        System.out.println(calculator.calc(1));
    }
}