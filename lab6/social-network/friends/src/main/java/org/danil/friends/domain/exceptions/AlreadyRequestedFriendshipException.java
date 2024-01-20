package org.danil.friends.domain.exceptions;

public class AlreadyRequestedFriendshipException extends FriendshipException {
    public AlreadyRequestedFriendshipException() {
    }

    public AlreadyRequestedFriendshipException(String message) {
        super(message);
    }

    public AlreadyRequestedFriendshipException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyRequestedFriendshipException(Throwable cause) {
        super(cause);
    }

    public AlreadyRequestedFriendshipException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
