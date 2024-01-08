package org.example.exceptions.account;

public class TerminalOperationException extends Exception {
    public TerminalOperationException() {
        super();
    }
    public TerminalOperationException(String message) {
        super(message);
    }
    public TerminalOperationException(Throwable cause) {
        super(cause);
    }
    public TerminalOperationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
