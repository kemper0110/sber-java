package org.danil;

import lombok.Getter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ScalableThreadPool implements ThreadPool {

    class BusyThread extends Thread {
        @Getter
        private volatile boolean busy = false;
        @Override
        public void run() {
            while (true) {
                Runnable task;
                synchronized (tasks) {
                    task = tasks.poll();
                }
                if (task == null) return;
                this.busy = true;
                task.run();
                this.busy = false;
            }
        }
    }

    final List<BusyThread> threads;
    final int minThreadCount, maxThreadCount;
    final private Queue<Runnable> tasks = new ArrayDeque<>(10);

    public ScalableThreadPool(int minThreadCount, int maxThreadCount) {
        this.minThreadCount = minThreadCount;
        this.maxThreadCount = maxThreadCount;
        this.threads = new ArrayList<>(maxThreadCount);

        for (int i = 0; i < minThreadCount; ++i)
            this.threads.add(new BusyThread());
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
         */
        if (threads.stream().allMatch(BusyThread::isBusy) && threads.size() < maxThreadCount) {
            synchronized (tasks) {
                if (tasks.isEmpty()) {
                    tasks.add(task);
                }
            }
        }
    }
}
