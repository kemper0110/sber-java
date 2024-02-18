package org.danil;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.Callable;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskTest {
    @RepeatedTest(300)
    void calledOnce_whenManyThreads() throws Exception {
        final var expectedResult = "hello-world";

        Callable<String> callableForTask = mock(Callable.class);
        when(callableForTask.call()).thenReturn(expectedResult);

        final var task = new Task<>(callableForTask);
        final Runnable threadTask = () -> {
            final var actualResult = task.get();
            assertEquals(expectedResult, actualResult);
        };
        final Thread.UncaughtExceptionHandler exceptionHandler = (th, ex) -> {
            assertNull(ex);
//            assertInstanceOf(TaskException.class, ex);
        };

        // тестирование при конкурентности в 1000 потоков
        final var threads = IntStream.range(0, 1000)
                .mapToObj(index -> new Thread(threadTask))
                .peek(t -> t.setUncaughtExceptionHandler(exceptionHandler))
                .toList();
        for (Thread thread : threads)
            thread.start();
        for (Thread thread : threads)
            assertDoesNotThrow(() -> thread.join());

        verify(callableForTask, times(1)).call();
    }

    @RepeatedTest(300)
    void catchException_whenThrows() throws Exception {
        Callable<String> callableForTask = mock(Callable.class);
        when(callableForTask.call()).thenThrow(new TaskException("Expected"));

        final var task = new Task<>(callableForTask);
        final Runnable threadTask = task::get;

        final var exceptionHandler = mock(Thread.UncaughtExceptionHandler.class);
        doAnswer((invocation) -> {
            Object ex = invocation.getArgument(1);
            assertInstanceOf(TaskException.class, ex);
            return null;
        }).when(exceptionHandler).uncaughtException(any(), any());

        // тестирование при конкурентности в 1000 потоков
        final var threads = IntStream.range(0, 1000)
                .mapToObj(index -> new Thread(threadTask))
                .peek(t -> t.setUncaughtExceptionHandler(exceptionHandler))
                .toList();
        for (Thread thread : threads)
            thread.start();
        for (Thread thread : threads)
            assertDoesNotThrow(() -> thread.join());

        verify(callableForTask, times(1)).call();
    }
}