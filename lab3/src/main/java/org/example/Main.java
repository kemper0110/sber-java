package org.example;

public class Main {
    public static void main(String[] args) {
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
}