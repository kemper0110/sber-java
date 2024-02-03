package org.danil;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.*;

public class ScalableTest {
    public static void main(String... args) {
        final var pool = new ScalableThreadPool(4, 6);

        for(int i = 0; i < 5; ++i) {
            final var taskId = i;
            System.out.println("add task #" + taskId);
            pool.execute(() -> {
                System.out.println(new GregorianCalendar().get(Calendar.SECOND) + "s: Task " + taskId + " started");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(new GregorianCalendar().get(Calendar.SECOND) + "s: Task " + taskId + " finished");
            });
        }

        pool.start();
        pool.shutdown();
    }
}
