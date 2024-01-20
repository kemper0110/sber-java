package org.danil.user.domain.requests;

public record RegisterUserRequest(String email, String firstname, String lastname, Integer age, Boolean sex,
                                  String avatar, String password) {
}
