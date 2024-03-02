package org.danil;

import org.danil.cache.Cachable;
import org.danil.source.PostgresDBSource;

import java.util.List;

public interface Calculator {
    @Cachable(value = PostgresDBSource.class)
    List<Integer> fibonacci(int n);
}
