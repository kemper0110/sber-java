package org.danil.cache;

import org.danil.storage.CacheStorage;
import org.danil.storage.InMemoryStorage;
import org.danil.storage.FileStorage;
import org.danil.storage.ZipFileStorage;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CachedInvocationHandler implements InvocationHandler {

    private final Map<Method, CacheStorage<List<Object>, Object>> methodStorageMap = new HashMap<>();
    private final Object delegate;

    public CachedInvocationHandler(Object delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!method.isAnnotationPresent(Cache.class)) return invoke(method, args);

        final var annotation = method.getAnnotation(Cache.class);

        if (!methodStorageMap.containsKey(method)) {
            System.out.println("Creating " + annotation.cacheType() + " storage for " + method.getName());
            final var storage = switch (annotation.cacheType()) {
                case FILE -> {
                    final var filename = annotation.filenamePrefix() + method.getName();
                    if (annotation.zip())
                        yield new ZipFileStorage<List<Object>, Object>(filename);
                    else
                        yield new FileStorage<List<Object>, Object>(filename);
                }
                case IN_MEMORY -> new InMemoryStorage<List<Object>, Object>();
            };
            methodStorageMap.put(method, storage);
        }

        final var storage = methodStorageMap.get(method);

        final var key = key(method, args, annotation.identityBy());

        if (!storage.containsKey(key)) {
            System.out.println("Delegation of " + method.getName());
            final var result = invoke(method, args);
            if (result instanceof List<?> && ((List<?>) result).size() > annotation.maxListSize())
                return result;
            storage.put(key, result);
        } else {
            System.out.println("Using cache of " + method.getName());
        }
        return storage.get(key);
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
        key.add(method.getName());
        // add only identified elements
        for (int i = 0; i < Math.min(identityBy.length, args.length); ++i)
            if (identityBy[i])
                key.add(args[i]);
        return key;
    }
}
