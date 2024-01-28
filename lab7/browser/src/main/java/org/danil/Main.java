package org.danil;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        final var scanner = new Scanner(System.in);

        System.out.print("Root directory(default=my): ");

        final var defaultDirectory = "file:///C:/Users/Danil/IdeaProjects/sber-java/lab7/plugins";
        final var inputDirectory = scanner.nextLine();
        final var rootDirectory = inputDirectory == "" ? defaultDirectory : inputDirectory;

        final var pluginManager = new PluginManager(rootDirectory);

        final var basicPlugin = pluginManager.load("BasicPlugin/target/BasicPlugin-1.0.jar", "org.danil.BasicPlugin");
        basicPlugin.doUsefull();

        final var hugePlugin = pluginManager.load("HugePlugin/target/HugePlugin-1.0.jar", "org.danil.HugePlugin");
        hugePlugin.doUsefull();
    }
}