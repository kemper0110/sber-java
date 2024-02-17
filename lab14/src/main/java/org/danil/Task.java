package org.danil;

import java.util.concurrent.Callable;

public class Task<T> {
    volatile T result;
    Callable<? extends T> callable;

    public Task(Callable<? extends T> callable) {
        this.callable = callable;
    }

    public T get() throws TaskException {
        if (result != null)
            return result;

        synchronized (this) {
            if (result == null) {
                try {
                    result = callable.call();
                } catch (Exception ex) {
                    throw new TaskException(ex);
                }
            }
        }
        return result;
    }
}

