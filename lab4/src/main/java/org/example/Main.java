package org.example;

import org.example.models.TerminalImpl;
import org.example.models.TerminalServerImpl;
import org.example.models.TerminalServerLockImpl;
import org.example.views.ConsoleTerminal;

public class Main {
    public static void main(String[] args) {
        final var consoleTerminal = new ConsoleTerminal(
                new TerminalImpl(
                        new TerminalServerImpl(
                                new TerminalServerLockImpl("1234", 10)
                        )
                )
        );
        consoleTerminal.run();
    }
}