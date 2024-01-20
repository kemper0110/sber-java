package org.danil.exceptions;

import org.danil.domain.Message;

public class UserIsNotFriendException extends MessageException {
    public UserIsNotFriendException() {
    }

    public UserIsNotFriendException(String message) {
        super(message);
    }

    public UserIsNotFriendException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserIsNotFriendException(Throwable cause) {
        super(cause);
    }

    public UserIsNotFriendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
