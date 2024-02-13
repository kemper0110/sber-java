package org.danil.lab13;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.danil.lab13.model.User;
import org.danil.lab13.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner {
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        final var userCount = userRepository.count().block();
        if (userCount > 0) {
            System.out.println("user seeder skipped, because of " + userCount + " users in db");
            return;
        }
        System.out.println("generating users");

        Faker faker = new Faker();

        final List<User> users = IntStream.range(0, 1000)
                .mapToObj(
                        i -> User.builder()
                                .firstname(faker.name().firstName())
                                .lastname(faker.name().lastName())
                                .job(faker.job().title())
                                .phone(faker.phoneNumber().phoneNumber())
                                .mail(faker.internet().emailAddress())
                                .image(Integer.toString(faker.random().nextInt(0, 4)))
                                .build()
                )
                .toList();
        final var saved = userRepository.saveAll(users).collectList().block();
        System.out.println("saved all: " + saved.size());
    }
}
