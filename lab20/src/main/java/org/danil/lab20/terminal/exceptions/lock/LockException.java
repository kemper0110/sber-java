package org.danil.lab20.terminal.exceptions.lock;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
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
