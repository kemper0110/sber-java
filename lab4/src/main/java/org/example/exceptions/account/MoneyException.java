package org.example.exceptions.account;

public class MoneyException extends TerminalOperationException {
    public MoneyException() {
        super();
    }
    public MoneyException(String message) {
        super(message);
    }
    public MoneyException(Throwable cause) {
        super(cause);
    }
    public MoneyException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
