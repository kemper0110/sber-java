package org.danil;

import org.danil.cache.CacheProxy;
import org.danil.service.Service;
import org.danil.service.ServiceImpl;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        CacheProxy cacheProxy = new CacheProxy();
        Service service = (Service) cacheProxy.cache(new ServiceImpl());

        // третий аргумент не учитывается
        System.out.println(service.run("aboba", 10, new Date(0L)));
        System.out.println(service.run("aboba", 10, new Date()));

        // список не кешируется
        System.out.println(service.longList(15));
        System.out.println(service.longList(15));
    }
}