package org.danil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ContextImpl implements Context {
    final private AtomicInteger completedTaskCount = new AtomicInteger(0);
    final private AtomicInteger failedTaskCount = new AtomicInteger(0);
    volatile private boolean finished = false;
    /**
     * writes HAPPENS-BEFORE {@link #finished} writes
     */
    private int interruptedTaskCount = 0;
    final Runnable[] packagedTasks;
    final Runnable callback;
    final BlockingQueue<Runnable> queue;

    public ContextImpl(BlockingQueue<Runnable> queue, int tasksCount, Runnable callback) {
        packagedTasks = new Runnable[tasksCount];
        this.callback = callback;
        this.queue = queue;
    }

    @Override
    public void interrupt() {
        // DCL
        if (!finished) {
            synchronized (this) {
                if (!finished) {
                    for (Runnable packagedTask : packagedTasks)
                        if (queue.remove(packagedTask))
                            ++interruptedTaskCount;
                    // interruptedTaskCount HAPPENS-BEFORE finished
                    finished = true;
                }
            }
        }
    }

    Runnable createTask(int index, Runnable runnable) {
        return packagedTasks[index] = () -> {

            try {
                runnable.run();
            } catch (Exception e) {
                failedTaskCount.incrementAndGet();
            } finally {
                // DCL
                if (completedTaskCount.incrementAndGet() == packagedTasks.length) {
                    synchronized (this) {
                        if (completedTaskCount.get() == packagedTasks.length) {
                            callback.run();
                            finished = true;
                        }
                    }
                }
            }

        };
    }


    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public int getCompletedTaskCount() {
        return completedTaskCount.get();
    }

    @Override
    public int getFailedTaskCount() {
        return failedTaskCount.get();
    }

    @Override
    public int getInterruptedTaskCount() {
        return interruptedTaskCount;
    }
}
