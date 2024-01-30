package org.danil;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class ScalableThreadPool implements ThreadPool {
    final List<Thread> threads;
    final private Queue<Runnable> tasks = new ArrayDeque<>(10);

    final Semaphore semaphore = new Semaphore(0);

    public ScalableThreadPool(int minThreadCount, int maxThreadCount) {
        try {
            semaphore.acquire(maxThreadCount - minThreadCount);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.threads = new ArrayList<>(maxThreadCount);

        for (int i = 0; i < minThreadCount; ++i)
            this.threads.add(new Thread(this::threadJob));
    }

    private void threadJob() {
        while (true) {
            Runnable task;
            synchronized (tasks) {
                task = tasks.poll();
            }
            if (task == null) return;
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            task.run();
            semaphore.release();
        }
    }

    @Override
    public void start() {
        for (Thread thread : threads)
            thread.start();
    }

    @Override
    public void execute(Runnable task) {
        /*
            Если все потоки заняты, то нужно создать новый.
            нужно ли определять забиты ли потоки.

         */
        if (semaphore.availablePermits() > 0) {
            synchronized (tasks) {
                if (tasks.isEmpty()) {
                    tasks.add(task);
                }
            }
        }
    }
}
