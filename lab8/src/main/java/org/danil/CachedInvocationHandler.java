package org.danil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CachedInvocationHandler implements InvocationHandler {

    private final Map<List<Object>, Object> resultByArg = new HashMap<>();
    private final Object delegate;

    public CachedInvocationHandler(Object delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!method.isAnnotationPresent(Cache.class)) return invoke(method, args);

        final var annotation = method.getAnnotation(Cache.class);
        final var key = key(method, args, annotation.identityBy());
        if (!resultByArg.containsKey(key)) {
            System.out.println("Delegation of " + method.getName());
            resultByArg.put(key, invoke(method, args));
        }
        return resultByArg.get(key);
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

    private List<Object> key(Method method, Object[] args, boolean[] identityBy) {
        List<Object> key = new ArrayList<>();
        key.add(method);
        // add only identified elements
        for(int i = 0; i < Math.min(identityBy.length, args.length); ++i)
            if(identityBy[i])
                key.add(args[i]);
        return key;
    }
}
