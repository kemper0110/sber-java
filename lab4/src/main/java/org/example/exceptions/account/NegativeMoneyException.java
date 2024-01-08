package org.example.exceptions.account;

public class NegativeMoneyException extends MoneyException {
    public NegativeMoneyException() {
        super("Сумма должна быть неотрицательной");
    }
    public NegativeMoneyException(String message) {
        super(message);
    }
    public NegativeMoneyException(Throwable cause) {
        super(cause);
    }
    public NegativeMoneyException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
