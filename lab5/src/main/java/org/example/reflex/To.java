package org.example.reflex;

import lombok.Setter;

public class To {
    @Setter
    private String name;
    @Setter
    private int count;
    @Setter
    private float length;
    @Setter
    private boolean good;

    @Override
    public String toString() {
        return "To{" +
                "name='" + name + '\'' +
                ", count=" + count +
                ", length=" + length +
                ", good=" + good +
                '}';
    }
}
