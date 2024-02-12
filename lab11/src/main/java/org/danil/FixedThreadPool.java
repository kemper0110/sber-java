package org.danil;

import java.util.*;
import java.util.concurrent.Semaphore;


public class FixedThreadPool implements ThreadPool {
    final private Queue<Runnable> tasks = new ArrayDeque<>(10);
    final private Semaphore workingThreads;

    class Worker extends Thread {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Runnable task;
                    synchronized (tasks) {
                        task = tasks.poll();
                        if (task == null) {
                            // если задач нет, то освобождаем семафор и ждем
                            workingThreads.release();
                            tasks.wait();
                        }
                    }
                    if (task == null) continue;
                    task.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    final private Worker[] workers;

    public FixedThreadPool(int threadCount) {
        this.workers = new Worker[threadCount];
        this.workingThreads = new Semaphore(threadCount);

        for (int i = 0; i < this.workers.length; ++i)
            this.workers[i] = new Worker();
    }


    @Override
    public void start() {
        for (Worker worker : workers)
            worker.start();
    }

    @Override
    public void execute(Runnable task) {
        // Если есть свободный поток, то для него захватывается семафор и кладется задача в очередь
        // иначе семафор не захватывается и задача просто кладется в очередь
        workingThreads.tryAcquire();
        synchronized (tasks) {
            tasks.add(task);
        }
    }

    @Override
    public void shutdown() {
        // ожидаем, когда все потоки закончат свои задачи и освободят семафор
        try {
            workingThreads.acquire(workers.length);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (Worker worker : workers) {
            if (!worker.isInterrupted()) {
                worker.interrupt();
            }
        }
    }
}
