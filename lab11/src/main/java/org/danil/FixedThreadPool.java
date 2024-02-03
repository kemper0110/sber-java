package org.danil;

import java.util.*;
import java.util.concurrent.Semaphore;


public class FixedThreadPool implements ThreadPool {
    final private Thread[] threads;
    final private Queue<Runnable> tasks = new ArrayDeque<>(10);
    final private Semaphore workingThreads;
    final int threadCount;

    public FixedThreadPool(int threadCount) {
        this.threads = new Thread[threadCount];
        this.workingThreads = new Semaphore(threadCount);
        this.threadCount = threadCount;

        for (int i = 0; i < this.threads.length; ++i)
            this.threads[i] = new Thread(this::threadJob);
    }

    private void threadJob() {
        while (true) {
            Runnable task;
            synchronized (tasks) {
                task = tasks.poll();
                if (task == null) {
                    try {
                        tasks.wait();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
            if (task == null)
                continue;
            try {
                workingThreads.acquire();
            } catch (InterruptedException e) {
                break;
            }
            task.run();
            workingThreads.release();
        }
    }

    @Override
    public void start() {
        for (Thread thread : threads)
            thread.start();
    }

    @Override
    public void execute(Runnable task) {
        synchronized (tasks) {
            tasks.add(task);
        }
    }

    @Override
    public void shutdown() {
        try {
            workingThreads.acquire(this.threadCount);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (Thread thread : threads) {
            if(!thread.isInterrupted()) {
                thread.interrupt();
            }
        }
    }
}
