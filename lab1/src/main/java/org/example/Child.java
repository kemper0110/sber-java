package org.example;

public class Child extends Parent{
    static {
        System.out.println("child:static 1");
    }
    {
        System.out.println("child:instance 1");
    }
    static {
        System.out.println("child:static 2");
    }
    public Child() {
        System.out.println("child:constructor");
    }
    public Child(String name) {
        super(name);
        System.out.println("child:name-constructor");
    }
    {
        System.out.println("child:instance 2");
    }
}
