package org.danil;

import java.util.List;
import java.util.Map;


class Main {

    record Person(int age, String name) {

    }

    public static void main(String... args) {
        final var someCollection = List.of(new Person(13, "Ivan"), new Person(21, "Danil"));

        final var m =
                Streams.of(someCollection)
            .filter(p -> p.age() > 20)
            .transform(p -> new Person(p.age() + 30, p.name()))
            .toMap(p -> p.name(), p -> p);
        System.out.println(m);
    }
}