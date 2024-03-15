package org.danil.lab20.terminal.exceptions.account;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
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
