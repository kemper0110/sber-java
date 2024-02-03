package org.danil;

public interface ThreadPool {
    void start();
    void execute(Runnable task);
    void shutdown();
}
