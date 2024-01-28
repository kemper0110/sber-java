package org.danil;

public class Main {
    public static void main(String[] args) {

        final var pluginManager = new PluginManager("file:///C:/Users/Danil/IdeaProjects/sber-java/lab7/plugins/target/plugins-1.0.jar");

        final var basicPlugin = pluginManager.load("", "org.danil.basic.BasicPlugin");
        basicPlugin.doUsefull();

        final var hugePlugin = pluginManager.load("", "org.danil.huge.HugePlugin");
        hugePlugin.doUsefull();
    }
}