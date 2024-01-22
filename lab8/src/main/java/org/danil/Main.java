package org.danil;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        CacheProxy cacheProxy = new CacheProxy();
        Service service = (Service) cacheProxy.cache(new ServiceImpl());

        System.out.println(service.run("aboba", 10, new Date(0L)));
        System.out.println(service.run("aboba", 10, new Date()));
    }
}