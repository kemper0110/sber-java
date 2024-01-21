package org.danil.friends.domain.exceptions;

public class FriendshipNotFoundException extends FriendshipException {
    public FriendshipNotFoundException() {
    }

    public FriendshipNotFoundException(String message) {
        super(message);
    }

    public FriendshipNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FriendshipNotFoundException(Throwable cause) {
        super(cause);
    }

    public FriendshipNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
