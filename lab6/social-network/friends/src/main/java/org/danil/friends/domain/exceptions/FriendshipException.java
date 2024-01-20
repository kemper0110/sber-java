package org.danil.friends.domain.exceptions;

public class FriendshipException extends Exception {
    public FriendshipException() {
    }

    public FriendshipException(String message) {
        super(message);
    }

    public FriendshipException(String message, Throwable cause) {
        super(message, cause);
    }

    public FriendshipException(Throwable cause) {
        super(cause);
    }

    public FriendshipException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
