package org.example.exceptions.lock;

public class LockException extends Exception {
    public LockException() {
        super();
    }
    public LockException(String message) {
        super(message);
    }
    public LockException(Throwable cause) {
        super(cause);
    }
    public LockException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
