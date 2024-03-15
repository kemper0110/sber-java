package org.danil.lab20.terminal.exceptions.account;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
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
