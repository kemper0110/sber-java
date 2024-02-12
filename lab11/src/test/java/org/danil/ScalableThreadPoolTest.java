package org.danil;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class ScalableThreadPoolTest {
    /**
     * Тест проверяет добавления нового потока для новой задачи при недостатке потоков
     */
    @Test
    void assertNewThreadCreate_WhenAllThreadsWorking_ForScalablePool() {
        System.out.println("test stated");
        // given
        // пул на 4 потока, расширяемый до 5
        final var pool = new ScalableThreadPool(4, 5);

        final var latch5 = new CountDownLatch(5);

        // 5 задач ожидающих 2 секунды каждая
        // 4 задачи будут запущеных на 4-х потоках
        // пятая создаст новый поток и выполнится вместе с остальными
        final var tasks = new ArrayList<Runnable>(5);
        for(int i = 0; i < 5; ++i) {
            final var taskId = i;
            tasks.add(() -> {
                System.out.println(new GregorianCalendar().get(Calendar.SECOND) + "s: Task " + taskId + " started");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                latch5.countDown();
                System.out.println(new GregorianCalendar().get(Calendar.SECOND) + "s: Task " + taskId + " finished");
            });
        }

        // when
        tasks.forEach(pool::execute);
        pool.start();

        // then
        assertTimeout(Duration.ofMillis(2500), () -> latch5.await());
        assertEquals(0, latch5.getCount());

        // finally
        pool.shutdown();
    }
}