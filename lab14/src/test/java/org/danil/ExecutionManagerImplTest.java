package org.danil;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExecutionManagerImplTest {

    @RepeatedTest(20)
    void tasksAreFinishedOrFailed_AfterExpectedTimeout() {
        // given
        final int taskCount = 50, threadSleep = 50, threadCount = 4, failedCount = 5;

        final var callback = mock(Runnable.class);

        final var tasks =
                IntStream.range(0, taskCount)
                        .mapToObj(index -> {
                            if (index < failedCount) {
                                return (Runnable) () -> {
                                    throw new RuntimeException("Task " + index + " expected to fail");
                                };
                            } else {
                                return (Runnable) () -> assertDoesNotThrow(() -> Thread.sleep(threadSleep));
                            }
                        }).toList();


        final var executionManager = new ExecutionManagerImpl(threadCount);
        final var taskArray = tasks.toArray(new Runnable[0]);

        // when
        final var context = executionManager.execute(callback, taskArray);

        // приблизительное время выполнения всех задач
        assertDoesNotThrow(() -> Thread.sleep((int) Math.ceil(1.5 * taskCount * threadSleep / threadCount)));

        // then
        assertTrue(context.isFinished());
        assertEquals(taskCount, context.getCompletedTaskCount());
        assertEquals(failedCount, context.getFailedTaskCount());
        assertEquals(0, context.getInterruptedTaskCount());

        verify(callback, times(1)).run();
    }

    @RepeatedTest(500)
    void testTaskCount_whenInterrupted() {
        // given
        final int taskCount = 50, threadSleep = 50, threadCount = 4;

        final var callback = mock(Runnable.class);

        final var tasks =
                IntStream.range(0, taskCount)
                        .mapToObj(index -> {
                            if (index < threadCount) {
                                return (Runnable) () -> assertDoesNotThrow(() -> Thread.sleep(threadSleep));
                            } else {
                                return (Runnable) () -> {};
                            }
                        }).toList();


        final var executionManager = new ExecutionManagerImpl(threadCount);
        final var taskArray = tasks.toArray(new Runnable[0]);

        // when
        final var context = executionManager.execute(callback, taskArray);
        context.interrupt();


        // then
        assertTrue(context.isFinished());
        assertEquals(0, context.getCompletedTaskCount());
        assertEquals(0, context.getFailedTaskCount());
        assertEquals(taskCount - threadCount, context.getInterruptedTaskCount());

        verify(callback, times(0)).run();
    }
}