package org.danil.cache;

import lombok.RequiredArgsConstructor;
import org.danil.source.Source;
import org.danil.serializer.Serializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@RequiredArgsConstructor
public class CachedInvocationHandler implements InvocationHandler {
    record CacheConfig(Source source, Serializer serializer) {

    }

    private final Map<Method, CacheConfig> methodStorageMap = new HashMap<>();

    private final Object delegate;
    private final CacheConfigFactory cacheConfigFactory;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!method.isAnnotationPresent(Cachable.class)) return invoke(method, args);

        final var annotation = method.getAnnotation(Cachable.class);

        if (!methodStorageMap.containsKey(method)) {
            System.out.println("Creating " + annotation.value() + " storage for " + method.getName());

            methodStorageMap.put(method, new CacheConfig(
                    cacheConfigFactory.createSource(annotation.value(), method.getName()),
                    cacheConfigFactory.createSerializer(annotation.serializer()))
            );
        }

        final var methodCacheConfig = methodStorageMap.get(method);

        final var key = methodCacheConfig.serializer.stringify(key(args));

        final var result = methodCacheConfig.source.get(key);
        if (result != null) {
            System.out.println("Using cache of " + method.getName());
            return methodCacheConfig.serializer.parse(result, method.getReturnType());
        } else {
            System.out.println("Delegation of " + method.getName());
            final var calculated = invoke(method, args);
            methodCacheConfig.source.put(key, methodCacheConfig.serializer.stringify(calculated));
            return calculated;
        }
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

    /**
     * Метод генерации ключа
     */
    private List<Object> key(Object[] args) {
        return new ArrayList<>(Arrays.asList(args));
    }
}
