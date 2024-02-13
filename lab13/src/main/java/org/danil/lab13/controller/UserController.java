package org.danil.lab13.controller;

import lombok.RequiredArgsConstructor;
import org.danil.lab13.model.User;
import org.danil.lab13.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping(produces = TEXT_EVENT_STREAM_VALUE)
    Flux<User> index(@RequestParam(value = "q", required = false, defaultValue = "") String name) {
        if(name == "")
            return userService.findAll();
        else
            return userService.findAllByName(name);
    }
}
