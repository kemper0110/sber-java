package org.danil.lab20.terminal.exceptions.account;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotEnoughMoneyException extends MoneyException {
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
