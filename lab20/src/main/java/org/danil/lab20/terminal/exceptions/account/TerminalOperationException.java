package org.danil.lab20.terminal.exceptions.account;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
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
