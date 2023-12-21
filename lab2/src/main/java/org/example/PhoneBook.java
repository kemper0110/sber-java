package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.ORDERED;

public class PhoneBook {
    @AllArgsConstructor
    private static class PhoneNode {
        private String surname;
        private String phone;
        protected PhoneNode next;
    }
    private PhoneNode list = null;
    public void add(String surname, String phone) {
        list = new PhoneNode(surname, phone, list);
    }
    public Stream<String> get(String surname) {
        final var spliterator = new Spliterators.AbstractSpliterator<String>(Long.MAX_VALUE, ORDERED){
            PhoneNode head = list;
            @Override
            public boolean tryAdvance(Consumer<? super String> action) {
                while (head != null && !head.surname.equals(surname))
                    head = head.next;
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
