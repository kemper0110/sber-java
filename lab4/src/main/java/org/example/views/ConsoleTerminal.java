package org.example.views;

import org.example.exceptions.account.TerminalOperationException;
import org.example.exceptions.lock.LockException;
import org.example.exceptions.account.NotEnoughMoneyException;
import org.example.exceptions.pin.PinMinLengthException;
import org.example.exceptions.pin.PinValidationException;
import org.example.models.Terminal;

import java.util.InputMismatchException;
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
        System.out.println("Программа - банковский терминал");

//        try {
//            terminal.unlock("1234");
//        } catch (LockException | PinValidationException e) {
//            throw new RuntimeException(e);
//        }
        while (true) {
            System.out.println("Требуется ввод 4-х значного пин-кода");
            System.out.println("Вводите пин-код по одной цифре");
            String pin = "";
            for (int i = 0; i < 4; ++i) {
                try {
                    final var input = scanner.next();
                    final var digit = Integer.parseInt(input);
                    if (digit > 9) {
                        System.out.println("Введено больше одной цифры");
                        --i; continue;
                    }
                    pin += digit;
                    System.out.println("Введенный пин-код: " + pin);
                } catch (NumberFormatException e) {
                    System.out.println("Вводите только цифры");
                }
            }

            try {
                if (terminal.unlock(pin))
                    break;
                System.out.println("Пин-код неверный");
            } catch (PinValidationException e) {
                System.out.println("Ошибка валидации пин-кода: " + e.getMessage());
            } catch (LockException e) {
                System.out.println("Ошибка попытки разблокировки: " + e.getMessage());
            }
        }

        System.out.println("Терминал успешно разблокирован!");

        final Pattern actionPattern = Pattern.compile("^\\s*(get)\\s*$"),
                consumerPattern = Pattern.compile("^\\s*(put|pull)\\s+(\\d+)\\s*$");

        String line;
        while ((line = scanner.nextLine()) != null) {
            if(line.isBlank() || line.isEmpty())
                continue;
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
                    if (method.equals("get")) {
                        System.out.println("У вас на счету: " + terminal.get());
                    }
                } else {
                    System.out.println("Действие в неверном формате: " + line);
                }
            } catch (LockException e) {
                System.out.println(e.getMessage());
            } catch (TerminalOperationException e) {
                System.out.println("Ошибка работы с терминалом: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Число введено в неверном формате");
            } catch (Exception e) {
                System.out.println("Внутренняя ошибка: наберите программиста");
            }
        }
    }
}
