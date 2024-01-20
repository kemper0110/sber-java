package org.danil.user.services;

import org.danil.user.domain.requests.LoginUserRequest;
import org.danil.user.domain.requests.RegisterUserRequest;
import org.danil.user.services.exceptions.UserAlreadyExistsException;
import org.danil.user.services.exceptions.UserNotFoundException;

public interface UserService {
    void register(RegisterUserRequest request) throws UserAlreadyExistsException;
    void login(LoginUserRequest request) throws UserNotFoundException;
}
