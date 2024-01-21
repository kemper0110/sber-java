package org.danil;

import java.lang.reflect.Proxy;

public class CacheProxy {

    public CacheProxy() {

    }

    public Object cache(Object object) {
        return Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                object.getClass().getInterfaces(),
                new CachedInvocationHandler(object));
    }
}
