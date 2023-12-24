package org.example;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.ORDERED;

public class PhoneBook {
    record PhoneNode(String phone, PhoneNode next) { }
    private final Map<String, PhoneNode> map = new HashMap<>();
    public void add(String surname, String phone) {
        map.put(surname, new PhoneNode(phone, map.get(surname)));
    }
    public Stream<String> get(String surname) {
        final var list = map.get(surname);
        final var spliterator = new Spliterators.AbstractSpliterator<String>(Long.MAX_VALUE, ORDERED){
            PhoneNode head = list;
            @Override
            public boolean tryAdvance(Consumer<? super String> action) {
                if (head == null)
                    return false;
                action.accept(head.phone);
                head = head.next;
                return true;
            }
        };
        return StreamSupport.stream(spliterator, false);
    }
}
