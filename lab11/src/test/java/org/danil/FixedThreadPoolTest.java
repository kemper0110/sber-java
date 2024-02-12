package org.danil;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class FixedThreadPoolTest {

    /**
     * Тест проверяет блокирование выполнения задач при недостатке потоков
     */
    @Test
    void assertWait_WhenAllThreadsWorking_ForFixedPool() {
        System.out.println("test stated");
        // given
        // пул на 4 потока
        final var pool = new FixedThreadPool(4);

        final var latch4 = new CountDownLatch(4);
        final var latch5 = new CountDownLatch(5);

        // 5 задач ожидающих 2 секунды каждая
        // 4 задачи должны завершиться через ~2 секунды,
        // пятая будет запущена только после завершения одной из предыдущих
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
                latch4.countDown();
                latch5.countDown();
                System.out.println(new GregorianCalendar().get(Calendar.SECOND) + "s: Task " + taskId + " finished");
            });
        }

        // when
        tasks.forEach(pool::execute);
        pool.start();

        // then
        // полсекунды это поправка на неточность Thread.sleep
        assertTimeout(Duration.ofMillis(2500), () -> latch4.await());
        assertEquals(0, latch4.getCount());
        assertEquals(1, latch5.getCount());
        assertTimeout(Duration.ofMillis(2500), () -> latch5.await());
        assertEquals(0, latch5.getCount());

        // finally
        pool.shutdown();
    }
}