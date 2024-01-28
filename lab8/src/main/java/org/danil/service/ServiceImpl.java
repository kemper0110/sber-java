package org.danil.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceImpl implements Service {
    @Override
    public List<String> run(String item, double value, Date date) {
        return List.of(item, Double.toString(value), date.toString());
    }

    @Override
    public List<String> work(String item) {
        return List.of(item);
    }

    @Override
    public List<Integer> longList(int length) {
        final var list = new ArrayList<Integer>();
        for(int i = 0; i < length; ++i)
            list.add(i);
        return list;
    }
}
