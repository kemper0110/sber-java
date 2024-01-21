package org.danil.friends.services;

import org.danil.friends.domain.Friendship;
import org.danil.friends.domain.requests.BreakUpRequest;
import org.danil.friends.domain.requests.RequestFriendshipRequest;
import org.danil.friends.repositories.FriendshipRepository;
import org.danil.user.domain.User;
import org.danil.user.repositories.UserRepository;
import org.danil.user.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FriendshipServiceTest {
    @Mock
    UserService userService;
    @Mock
    FriendshipRepository friendshipRepository;
    @InjectMocks
    FriendshipServiceImpl friendshipService;

    @Test
    void requestFriendship_ok() {
        when(userService.getCurrent()).thenReturn(new User(1L));
        when(friendshipRepository.getByUsers(1L, 2L)).thenReturn(null);

        assertDoesNotThrow(() -> friendshipService.requestFriendship(
                new RequestFriendshipRequest(new User(2L))
        ));

        verify(friendshipRepository, times(1)).save(any(Friendship.class));
    }

    @Test
    void breakUp_ok() {
        when(userService.getCurrent()).thenReturn(new User(1L));
        when(friendshipRepository.getByUsers(1L, 2L)).thenReturn(new Friendship(new User(1L), new User(2L), new Date(), Friendship.Status.ACTIVE));

        assertDoesNotThrow(() -> friendshipService.breakUp(new BreakUpRequest(new User(2L))));

        verify(friendshipRepository, times(1)).deleteByUsers(1L, 2L);
    }

    @Test
    void acceptFriendship_ok() {
        when(userService.getCurrent()).thenReturn(new User(1L));
        when(friendshipRepository.getByUsers(1L, 2L)).thenReturn(new Friendship(new User(1L), new User(2L), new Date(), Friendship.Status.REQUESTED));

        assertDoesNotThrow(() -> friendshipService.breakUp(new BreakUpRequest(new User(2L))));

        verify(friendshipRepository, times(1)).deleteByUsers(1L, 2L);
    }
}