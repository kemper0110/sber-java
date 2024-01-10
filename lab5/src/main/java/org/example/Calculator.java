package org.example;

import org.example.annotations.Cache;
import org.example.annotations.Metric;

public interface Calculator {
    @Metric
    @Cache
    int calc(int arg);
}

