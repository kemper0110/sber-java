package org.danil;

public interface ExecutionManager {
    Context execute(Runnable callback, Runnable... tasks);
}
