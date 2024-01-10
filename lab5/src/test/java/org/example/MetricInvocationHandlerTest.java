package org.example;

import org.example.annotations.MetricInvocationHandler;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

class MetricInvocationHandlerTest {
    @Test
    void test() {
        Calculator delegate = new FactorialCalculator();
        Calculator calculator = (Calculator) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                delegate.getClass().getInterfaces(),
                new MetricInvocationHandler(delegate)
        );

        System.out.println(calculator.calc(1));
        System.out.println(calculator.calc(10));
        System.out.println(calculator.calc(10));
        System.out.println(calculator.calc(6));
        System.out.println(calculator.calc(2));
    }
}