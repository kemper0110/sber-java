package org.example;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.stream.Stream;

public class PhoneBook {
    @AllArgsConstructor
    private static class PhoneNode {
        private String surname;
        private String phone;
    }
    private final ArrayList<PhoneNode> list = new ArrayList<>();
    public void add(String surname, String phone) {
        list.add(new PhoneNode(surname, phone));
    }
    public Stream<String> get(String surname) {
        return list.stream()
                .filter(node -> node.surname.equals(surname))
                .map(node -> node.phone);
    }
}
