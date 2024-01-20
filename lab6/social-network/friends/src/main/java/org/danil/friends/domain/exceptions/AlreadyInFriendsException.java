package org.danil.friends.domain.exceptions;

public class AlreadyInFriendsException extends FriendshipException {
    public AlreadyInFriendsException() {
    }

    public AlreadyInFriendsException(String message) {
        super(message);
    }

    public AlreadyInFriendsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyInFriendsException(Throwable cause) {
        super(cause);
    }

    public AlreadyInFriendsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
