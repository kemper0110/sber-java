package org.danil;

import lombok.Getter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ScalableThreadPool implements ThreadPool {
    final private Semaphore workingThreads;

    @Getter
    class Worker extends Thread {
        final Runnable initialTask;

        Worker(Runnable initialTask) {
            this.initialTask = initialTask;
        }

        @Override
        public void run() {
            // для вновь созданного потока может быть сразу передана задача
            // она выполняется немедленно
            if (initialTask != null)
                initialTask.run();
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Runnable task;
                    synchronized (tasks) {
                        task = tasks.poll();
                        if (task == null) {
                            // если задач нет, то освобождаем семафор и ждем
                            System.out.println(Thread.currentThread().getName() + " is waiting");
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

    final List<Worker> workers;
    final int minThreadCount, maxThreadCount;
    final private Queue<Runnable> tasks = new ArrayDeque<>(10);

    public ScalableThreadPool(int minThreadCount, int maxThreadCount) {
        this.minThreadCount = minThreadCount;
        this.maxThreadCount = maxThreadCount;
        this.workingThreads = new Semaphore(maxThreadCount);
        try {
            this.workingThreads.acquire(maxThreadCount - minThreadCount);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.workers = new ArrayList<>(maxThreadCount);

        for (int i = 0; i < minThreadCount; ++i)
            this.workers.add(new Worker(null));
    }

    @Override
    public void start() {
        for (Worker worker : workers)
            worker.start();
    }

    @Override
    public void execute(Runnable task) {
        System.out.println("available threads: " + workingThreads.availablePermits());
        // если удалось захватить семафор, то был свободный поток
        // он заберет для выполнения добавленную в очередь задачу
        if (workingThreads.tryAcquire()) {
            synchronized (tasks) {
                tasks.add(task);
            }
        }
        // иначе создаем новый поток с задачей, минуя очередь
        else if (workers.size() < maxThreadCount) {
            System.out.println("added thread");
            workers.add(new Worker(task));
        }
    }

    @Override
    public void shutdown() {
        System.out.println("shutdown has:" + workingThreads.availablePermits() + " and requires:" + workers.size());
        // ожидаем, когда все потоки закончат свои задачи и освободят семафор
        try {
            workingThreads.acquire(workers.size());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("shutdown waiting done");
        for (Thread thread : workers) {
            if (!thread.isInterrupted()) {
                thread.interrupt();
            }
        }
    }
}
