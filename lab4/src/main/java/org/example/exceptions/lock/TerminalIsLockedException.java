package org.example.exceptions.lock;

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
