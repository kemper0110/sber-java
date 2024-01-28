package org.danil.service;

import org.danil.cache.Cache;

import java.util.Date;
import java.util.List;

public interface Service {
    @Cache(cacheType = Cache.CacheType.FILE, identityBy = {true, true, false}, filenamePrefix = "cached_")
    List<String> run(String item, double value, Date date);

    @Cache()
    List<String> work(String item);

    @Cache(maxListSize = 10)
    List<Integer> longList(int length);
}
