package org.danil;

import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class GC {
    private static final Random random = new Random();
    public static void main(String... args) throws InterruptedException {
        final var scanner = new Scanner(System.in);
        System.out.println("We are getting started");
        scanner.nextLine();

        final var trashSize = 10_000;
        final var maxTrashboxCount = 2 * 1024 / 8 * 1024 / trashSize * 1024; // 2gb limit

        final var trashbox = new LinkedList<long[]>();
        for(long i = 0; i < maxTrashboxCount;++i) {
            final var trash = new long[trashSize];
            if(random.nextInt(0, 101) > 50)
                trashbox.addLast(trash);
            if(random.nextInt(0, 101) > 80 && !trashbox.isEmpty())
                trashbox.removeFirst();
            Thread.sleep(2);
            System.out.println(i + " 8kb trash created");
        }
    }
}
