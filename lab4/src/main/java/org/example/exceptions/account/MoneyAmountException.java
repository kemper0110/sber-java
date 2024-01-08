package org.example.exceptions.account;

public class MoneyAmountException extends MoneyException {
    public MoneyAmountException() {
        super();
    }
    public MoneyAmountException(String message) {
        super(message);
    }
    public MoneyAmountException(Throwable cause) {
        super(cause);
    }
    public MoneyAmountException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
