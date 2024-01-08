package org.example.exceptions.account;

public class NotDivisibleException extends MoneyException{
    public NotDivisibleException() {
        super("Сумма должна быть кратна 100");
    }
    public NotDivisibleException(String message) {
        super(message);
    }
    public NotDivisibleException(Throwable cause) {
        super(cause);
    }
    public NotDivisibleException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
