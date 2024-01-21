package org.danil.friends.services;

import org.danil.friends.domain.Friendship;
import org.danil.friends.domain.exceptions.AlreadyInFriendsException;
import org.danil.friends.domain.exceptions.AlreadyRequestedFriendshipException;
import org.danil.friends.domain.exceptions.FriendshipException;
import org.danil.friends.domain.exceptions.FriendshipNotFoundException;
import org.danil.friends.domain.requests.AcceptFriendshipRequest;
import org.danil.friends.domain.requests.BreakUpRequest;
import org.danil.friends.domain.requests.RequestFriendshipRequest;
import org.danil.friends.repositories.FriendshipRepository;
import org.danil.user.repositories.UserRepository;
import org.danil.user.services.UserService;

import java.util.Date;

public class FriendshipServiceImpl implements FriendshipService {
    UserService userService;
    FriendshipRepository friendshipRepository;

    @Override
    public void requestFriendship(RequestFriendshipRequest request) throws FriendshipException {
        final var from = userService.getCurrent();
        final var to = request.user();

        final var existingFriendship = friendshipRepository.getByUsers(from.getId(), to.getId());
        if(existingFriendship != null) {
            if(existingFriendship.getStatus() == Friendship.Status.REQUESTED)
                throw new AlreadyRequestedFriendshipException();
            else
                throw new AlreadyInFriendsException();
        }

        friendshipRepository.save(new Friendship(from, to, new Date(), Friendship.Status.REQUESTED));
    }

    @Override
    public void breakUp(BreakUpRequest request) throws FriendshipException {
        final var from = userService.getCurrent();
        final var to = request.user();

        final var existingFriendship = friendshipRepository.getByUsers(from.getId(), to.getId());
        if(existingFriendship == null)
            throw new FriendshipNotFoundException();

        friendshipRepository.deleteByUsers(from.getId(), to.getId());
    }

    @Override
    public void acceptFriendship(AcceptFriendshipRequest request) throws FriendshipException {
        final var from = userService.getCurrent();
        final var to = request.user();

        final var existingFriendship = friendshipRepository.getByUsers(from.getId(), to.getId());
        if(existingFriendship == null)
            throw new FriendshipNotFoundException();

        if(existingFriendship.getStatus() == Friendship.Status.ACTIVE)
            throw new AlreadyInFriendsException();

        friendshipRepository.save(new Friendship(existingFriendship.getFirst(), existingFriendship.getSecond(), new Date(), Friendship.Status.ACTIVE));
    }
}
