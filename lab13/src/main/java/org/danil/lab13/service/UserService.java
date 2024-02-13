package org.danil.lab13.service;

import lombok.RequiredArgsConstructor;
import org.danil.lab13.model.User;
import org.danil.lab13.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public Flux<User> findAll() {
        return userRepository.findAll().take(10).delayElements(Duration.ofMillis(300));
    }
    public Flux<User> findAllByName(String name) {
        return userRepository.findAllByName(name).delayElements(Duration.ofMillis(300));
    }
}
