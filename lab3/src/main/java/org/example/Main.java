package org.example;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    static void task1() {
        CountMap<Number> map = new CountMapImpl<>();

        map.add(10);
        map.add(10);
        map.add(5);
        map.add(6);
        map.add(5);
        map.add(10);


        System.out.println(map.getCount(5));  // 2
        System.out.println(map.getCount(6));  // 1
        System.out.println(map.getCount(10)); // 3
    }
    public static void main(String[] args) {
        task2();
    }

    static void task2() {
        var dest = new ArrayList<Base>();
        dest.add(new Base());
        CollectionUtils.addAll(List.of(new Cringe(), new Base()), dest);
        System.out.println(dest);
        System.out.println(CollectionUtils.range(Arrays.asList(8,1,3,5,6, 4), 3, 6));
    }
}

class Base {
    @Override
    public String toString() {
        return "Base";
    }
}

class Cringe extends Base {
    @Override
    public String toString() {
        return "Cringe";
    }
}