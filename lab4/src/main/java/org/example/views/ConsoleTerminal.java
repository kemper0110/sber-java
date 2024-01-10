package org.example.views;

import org.example.exceptions.lock.LockException;
import org.example.exceptions.account.NotEnoughMoneyException;
import org.example.exceptions.pin.PinMinLengthException;
import org.example.exceptions.pin.PinValidationException;
import org.example.models.Terminal;

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

        final Pattern actionPattern = Pattern.compile("^\\s*(get|unlock)\\s*$"),
                consumerPattern = Pattern.compile("^\\s*(put|pull)\\s+(\\d+)\\s*$");

        System.out.println("Программа - банковский терминал");

        String line;
        while ((line = scanner.nextLine()) != null) {
            try {
                final var consumerMatches = consumerPattern.matcher(line);
                final var actionMatches = actionPattern.matcher(line);

                if (consumerMatches.find()) {
                    final var method = consumerMatches.group(1);
                    final var arg = Long.parseLong(consumerMatches.group(2));

                    switch (method) {
                        case "put" -> System.out.println("Теперь у вас на счету: " + terminal.put(arg));
                        case "pull" -> {
                            try {
                                System.out.println("Теперь у вас на счету: " + terminal.pull(arg));
                            } catch (NotEnoughMoneyException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        default -> System.out.println("Неверное имя метода");
                    }
                } else if (actionMatches.find()) {
                    final var method = actionMatches.group(1);
                    switch (method) {
                        case "get" -> System.out.println("У вас на счету: " + terminal.get());
                        case "unlock" -> {
                            System.out.println("Вводите пин-код. Каждая цифра на новой строке.");
                            String pin = "";
                            while (true) {
                                final var newPinCode = pin + scanner.nextLine();
                                System.out.println("Введенный пин-код: " + newPinCode);
                                try {
                                    if (terminal.unlock(newPinCode)) {
                                        System.out.println("Терминал успешно разблокирован!");
                                    } else {
                                        System.out.println("Пин-код неверный");
                                        break;
                                    }
                                    pin = newPinCode;
                                } catch (PinMinLengthException e) {
                                    // wait for full length
                                } catch (PinValidationException e) {
                                    System.out.println("Ошибка валидации пин-кода: " + e.getMessage());
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Действие в неверном формате");
                }
            } catch (LockException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Число введено в неверном формате");
            } catch (Exception e) {
                System.out.println("Внутренняя ошибка: наберите программиста");
            }
        }
    }
}
