package org.danil;

import java.util.concurrent.Callable;

public class Task<T> {
    enum Status {EMPTY, RESULT, EXCEPTION}

    T result;
    TaskException exception;
    volatile Status status = Status.EMPTY;
    Callable<? extends T> callable;

    public Task(Callable<? extends T> callable) {
        this.callable = callable;
    }

    public T get() throws TaskException {
        if (status == Status.EMPTY) {
            synchronized (this) {
                if (status == Status.EMPTY) {
                    try {
                        result = callable.call();
                        status = Status.RESULT;
                    } catch (Exception ex) {
                        exception = new TaskException(ex);
                        status = Status.EXCEPTION;
                    }
                }
            }
        }
        if (status == Status.EXCEPTION)
            throw exception;
        return result;
    }
}

