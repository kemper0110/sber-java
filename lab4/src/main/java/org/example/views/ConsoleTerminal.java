package org.example.views;

import org.example.exceptions.lock.LockException;
import org.example.exceptions.NotEnoughMoneyException;
import org.example.exceptions.pin.PinValidationException;
import org.example.models.Terminal;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

// Консольный вариант интерфейса вывода сообщений
public class ConsoleTerminal {
    private final Terminal terminal;

    public ConsoleTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public void run() {
        final var scanner = new Scanner(System.in);

        final Pattern actionPattern = Pattern.compile("^\\w(get|unlock)\\w$"),
                consumerPattern = Pattern.compile("^\\w(put|pull)\\w(\\d+)\\w$");

        String line;
        while ((line = scanner.nextLine()) != null) {
            System.out.println(line);

            try {
                final var consumerMatches = consumerPattern.matcher(line);
                final var actionMatches = actionPattern.matcher(line);

                if (consumerMatches.find()) {
                    final var method = consumerMatches.group(0);
                    final var arg = Long.parseLong(consumerMatches.group(1));

                    switch (method) {
                        case "put":
                            System.out.println("Теперь у вас на счету: " + terminal.put(arg));
                            break;
                        case "pull":
                            try {
                                System.out.println("Теперь у вас на счету: " + terminal.pull(arg));
                            } catch (NotEnoughMoneyException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        default:
                            System.out.println("Неверное имя метода");
                    }
                } else if (actionMatches.find()) {
                    final var method = actionMatches.group(0);
                    switch (method) {
                        case "get":
                            System.out.println("У вас на счету: " + terminal.get());
                            break;
                        case "unlock":
                            String pin = "";
                            do pin += (char) System.in.read();
                            while (!terminal.unlock(pin));
                            break;
                    }
                } else {
                    System.out.println("Действие в неверном формате");
                }
            } catch (LockException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Число введено в неверном формате");
            } catch (IOException e) {
                System.out.println("Ошибка чтения ввода");
            } catch (PinValidationException e) {
                System.out.println("Ошибка валидации пин-кода: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Внутренняя ошибка: наберите программиста");
            }
        }
    }
}
