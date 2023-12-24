package org.example;

import java.util.HashMap;
import java.util.Map;

public class PhoneBook {
    record PhoneNode(String phone, PhoneNode next) { }
    private final Map<String, PhoneNode> map = new HashMap<>();
    public void add(String surname, String phone) {
        map.put(surname, new PhoneNode(phone, map.get(surname)));
    }
    public PhoneNode get(String surname) {
        return map.get(surname);
    }
}
