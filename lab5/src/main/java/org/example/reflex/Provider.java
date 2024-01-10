package org.example.reflex;


import lombok.Getter;

public class Provider {
    String getBobr() {
        return "bobr";
    }

    String getBober() {
        return "bober";
    }

    String isBober() {
        return "true";
    }

    @Getter
    private int counter;

    public static final String SUNDAY = "SUNDAY";
    public static final String MONDAY = "MONDAY";
    public static final String TUESDAY = "TUESDAY";
    public static final String SATURDAY = "SATURDAY";
}
