package org.danil;

public class Main {
    public static void main(String[] args) {

        final var pluginManager = new PluginManager("file:///C:/Users/Danil/IdeaProjects/sber-java/lab7/plugins");

        final var basicPlugin = pluginManager.load("BasicPlugin/target/BasicPlugin-1.0.jar", "org.danil.BasicPlugin");
        basicPlugin.doUsefull();

        final var hugePlugin = pluginManager.load("HugePlugin/target/HugePlugin-1.0.jar", "org.danil.HugePlugin");
        hugePlugin.doUsefull();
    }
}