package org.danil.lab20.terminal.exceptions.lock;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccountIsLockedException extends LockException {
    public AccountIsLockedException(int elapsed) {
        super("Аккаунт заблокирован: повторите попытку ввода пин-кода через %d секунд".formatted(elapsed));
    }
    public AccountIsLockedException() {
        super();
    }
    public AccountIsLockedException(String message) {
        super(message);
    }
    public AccountIsLockedException(Throwable cause) {
        super(cause);
    }
    public AccountIsLockedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
