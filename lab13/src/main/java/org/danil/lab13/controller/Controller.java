package org.danil.lab13.controller;

import lombok.RequiredArgsConstructor;
import org.danil.lab13.model.Request;
import org.danil.lab13.service.RequestService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {
    private final RequestService requestService;
    @GetMapping(produces = TEXT_EVENT_STREAM_VALUE)
    Flux<Request> index() {
        return requestService.findAll();
    }

    @PostMapping
    Mono<Request> store(@RequestBody Request request) {
        return requestService.addOne(request);
    }
}
