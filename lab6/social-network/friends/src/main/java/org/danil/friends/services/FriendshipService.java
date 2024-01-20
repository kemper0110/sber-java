package org.danil.friends.services;

import org.danil.friends.domain.exceptions.FriendshipException;
import org.danil.friends.domain.requests.AcceptFriendshipRequest;
import org.danil.friends.domain.requests.BreakUpRequest;
import org.danil.friends.domain.requests.RequestFriendshipRequest;

public interface FriendshipService {
    void requestFriendship(RequestFriendshipRequest request) throws FriendshipException;
    void breakUp(BreakUpRequest request) throws FriendshipException;
    void acceptFriendship(AcceptFriendshipRequest request) throws FriendshipException;
}
