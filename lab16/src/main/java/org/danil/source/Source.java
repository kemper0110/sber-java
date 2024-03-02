package org.danil.source;

public interface Source {
    void put(String args, String result);
    String get(String args);
}
