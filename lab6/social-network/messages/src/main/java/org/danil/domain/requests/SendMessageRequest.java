package org.danil.domain.requests;

import org.danil.user.domain.User;

public record SendMessageRequest(User sender, User receiver, String message) {
}
