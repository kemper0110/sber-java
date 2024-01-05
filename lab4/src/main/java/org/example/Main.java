package org.example;

import org.example.models.TerminalImpl;
import org.example.models.TerminalServerImpl;
import org.example.views.ConsoleTerminal;

public class Main {
    public static void main(String[] args) {
        final var terminalServer = new TerminalServerImpl();
        final var terminal = new TerminalImpl(terminalServer);
        final var consoleTerminal = new ConsoleTerminal(terminal);
        consoleTerminal.run();
    }
}