package org.danil.lab20.terminal.exceptions.account;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
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
