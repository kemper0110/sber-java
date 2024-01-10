package org.example.annotations;

import org.example.annotations.Cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MetricInvocationHandler implements InvocationHandler {
    private final Object delegate;

    public MetricInvocationHandler(Object delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!method.isAnnotationPresent(Cache.class)) return invoke(method, args);

        final var start = System.currentTimeMillis();
        final var result = invoke(method, args);
        final var elapsed = System.currentTimeMillis() - start;
        System.out.println("Metric of " + method.getName() + ": " + elapsed);

        return result;
    }

    private Object invoke(Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(delegate, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Impossible", e);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
