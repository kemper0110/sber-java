package org.danil;

import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) {
        CacheProxy cacheProxy = new CacheProxy();
        Service service = (Service) cacheProxy.cache(new ServiceImpl());
    }
}