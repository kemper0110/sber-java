package org.danil.friends.repositories;

import org.danil.friends.domain.Friendship;
import org.danil.user.domain.User;

import java.util.List;

public interface FriendshipRepository {
    Friendship save(Friendship friendship);
    void deleteByUsers(User first, User second);
    List<Friendship> getAll();
    List<Friendship> getAllByUser(User user);
}
