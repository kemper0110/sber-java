package org.example.exceptions;

public class NotEnoughMoneyException extends Exception {
    public NotEnoughMoneyException() {
        super("У вас не хватает денег на счету");
    }
    public NotEnoughMoneyException(String message) {
        super(message);
    }
    public NotEnoughMoneyException(Throwable cause) {
        super(cause);
    }
    public NotEnoughMoneyException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
