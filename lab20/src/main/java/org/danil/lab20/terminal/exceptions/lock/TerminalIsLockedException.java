package org.danil.lab20.terminal.exceptions.lock;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TerminalIsLockedException extends LockException {
    public TerminalIsLockedException() {
        super("Терминал заблокирован: разблокируйте используя пин-код");
    }
    public TerminalIsLockedException(String message) {
        super(message);
    }
    public TerminalIsLockedException(Throwable cause) {
        super(cause);
    }
    public TerminalIsLockedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
