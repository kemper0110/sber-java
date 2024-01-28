package org.danil.cache;

import lombok.NoArgsConstructor;

import java.lang.reflect.Proxy;

@NoArgsConstructor
public class CacheProxy {
    public Object cache(Object object) {
        return Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                object.getClass().getInterfaces(),
                new CachedInvocationHandler(object));
    }
}
