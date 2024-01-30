package org.danil;

import java.util.*;


public class FixedThreadPool implements ThreadPool {
    final private Thread[] threads;
    final private Queue<Runnable> tasks = new ArrayDeque<>(10);

    public FixedThreadPool(int threadCount) {
        this.threads = new Thread[threadCount];

        for (int i = 0; i < this.threads.length; ++i)
            this.threads[i] = new Thread(this::threadJob);
    }

    private void threadJob() {
        while (true) {
            Runnable task;
            synchronized (tasks) {
                task = tasks.poll();
            }
            if (task == null) return;
            task.run();
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
}
