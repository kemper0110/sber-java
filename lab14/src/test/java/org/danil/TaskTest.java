package org.danil;

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
    @Mock
    Callable<String> callableForTask;

    @ParameterizedTest
    @ValueSource(ints = {5_000})
    void testTask(int iterations) throws Exception {
        final var expectedResult = "hello-world";

        when(callableForTask.call()).thenReturn(expectedResult);

        for (int i = 0; i < iterations; ++i) {

            final var task = new Task<>(callableForTask);
            final Runnable threadTask = () -> {
                final var actualResult = task.get();
                assertEquals(expectedResult, actualResult);
            };
            final Thread.UncaughtExceptionHandler exceptionHandler = (th, ex) -> {
                assertNull(ex);
//            assertInstanceOf(TaskException.class, ex);
            };

            IntStream.range(0, 20)
                    .mapToObj(_i -> new Thread(threadTask))
                    .peek(t -> t.setUncaughtExceptionHandler(exceptionHandler))
                    .peek(Thread::start)
                    .forEach(t -> assertDoesNotThrow(() -> t.join()));

        }
        verify(callableForTask, times(iterations)).call();
    }
}