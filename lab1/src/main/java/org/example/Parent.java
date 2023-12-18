package org.example;

public abstract class Parent {
    private String name = null;
    static {
        System.out.println("parent:static 1");
    }
    {
        System.out.println("parent:instance 1");
    }
    static {
        System.out.println("parent:static 2");
    }
    public Parent() {
        System.out.println("parent:constructor 2");
    }
    {
        System.out.println("parent:instance 2");
    }
    public Parent(String name) {
        this.name = name;
        System.out.println("parent:name-constructor");
    }
}
