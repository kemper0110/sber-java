package org.danil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ExecutionManagerImpl implements ExecutionManager {
    final LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    final ExecutorService executorService;

    public ExecutionManagerImpl(int nThreads) {
        this.executorService = new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, queue);
    }

    @Override
    public Context execute(final Runnable callback, final Runnable... tasks) {
        final var context = new ContextImpl(queue, tasks.length, callback);

        queue.addAll(
                IntStream.range(0, tasks.length)
                        .mapToObj(index -> context.createTask(index, tasks[index]))
                        .peek(executorService::execute)
                        .toList()
        );
        return context;
    }
}
