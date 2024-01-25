package org.danil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Supplier;

/*
    Дан файл содержащий несколько случайных натуральных чисел от 1 до 50.
    Необходимо написать многопоточное приложение, которое параллельно рассчитает и выведет в консоль факториал для каждого числа из файла.
 */
class Main {

    static List<Long> readInputValues() {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("a.txt");
        assert inputStream != null;
        final var scanner = new Scanner(inputStream);
        final var values = new ArrayList<Long>(50);
        while (true) {
            int value;
            try {
                value = scanner.nextInt();
                values.add((long) value);
            } catch (Exception e) {
                break;
            }
        }
        return values;
    }

    public static void main(String... args) {
        final var values = readInputValues();

        final Function<Long, Long> factorial = arg -> {
            var result = 1L;
            while (arg > 1L)
                result *= arg--;
            return result;
        };

        final var tasks = values.stream().map(value -> (Supplier<Long>) () -> factorial.apply(value)).toList();

        final var threadCount = Runtime.getRuntime().availableProcessors();
        System.out.println("Available processors: " + threadCount);
        final var tasksPerThread = tasks.size() / threadCount;

        for (int i = 0; i < threadCount; i++) {
            final var threadId = i;
            new Thread(() -> {
                final var startId = threadId * tasksPerThread;
                final var endId = threadId == threadCount - 1 ? tasks.size() : (
                        (threadId + 1) * tasksPerThread
                );
                for (int j = startId; j < endId; j++)
                    System.out.println("Thread " + Thread.currentThread().getName() + " calculated: " + tasks.get(j).get());
            }).start();
        }
    }
}