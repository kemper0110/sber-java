package org.danil.user.repositories;

import org.danil.user.domain.User;
import java.util.List;

public interface UserRepository {
    User save(User user);
    List<User> getAll();
    User getById(Long id);
}
