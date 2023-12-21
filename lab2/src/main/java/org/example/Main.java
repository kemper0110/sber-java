package org.example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Main {
    static void task1() {
        final var list = Arrays.asList(
                "slovo1", "slovo2", "slovo3", "slovo4", "slovo4",
                "slovo1", "slovo2", "slovo3", "slovo4", "slovo4",
                "slovo1", "slovo2", "slovo3", "slovo4", "slovo4",
                "slovo1", "slovo2", "slovo3", "slovo4", "slovo4"
        );
        System.out.println("list: " + list);

        final var unique = new HashSet<>(list);
        System.out.println("unique: " + unique);

        final var counted = new HashMap<String, Integer>();
        list.forEach(s -> counted.put(s, counted.getOrDefault(s, 0) + 1));
        System.out.println("counted: " + counted);
    }
    static void task2() {
        final var phoneBook = new PhoneBook();
        phoneBook.add("aboba", "1234");
        phoneBook.add("cringe", "123");
        phoneBook.add("aboba", "5678");
        phoneBook.add("cringe", "999");
        phoneBook.add("cringe", "555");
        phoneBook.add("aaaaaa", "7777");


        System.out.print("aboba: ");
        phoneBook.get("aboba").forEach(phone -> System.out.print(phone + " "));
        System.out.println();

        System.out.print("cringe: ");
        phoneBook.get("cringe").forEach(phone -> System.out.print(phone + " "));
        System.out.println();

        System.out.print("aaaaaa: ");
        phoneBook.get("aaaaaa").forEach(phone -> System.out.print(phone + " "));
        System.out.println();
    }

    public static void main(String[] args) {
        task1();
        task2();
    }
}