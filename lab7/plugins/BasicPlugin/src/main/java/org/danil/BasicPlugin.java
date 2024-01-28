package org.danil;

import org.danil.Plugin;

public class BasicPlugin implements Plugin {
    @Override
    public void doUsefull() {
        System.out.println("Hello, I'm basic plugin");
    }
}
