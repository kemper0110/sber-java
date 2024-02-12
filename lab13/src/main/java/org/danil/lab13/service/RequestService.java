package org.danil.lab13.service;

import lombok.RequiredArgsConstructor;
import org.danil.lab13.model.Request;
import org.danil.lab13.repository.RequestRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;

    public Flux<Request> findAll() {
        return requestRepository.findAll().delayElements(Duration.ofMillis(500));
    }

    public Mono<Request> addOne(Request request) {
        return requestRepository.save(request);
    }
}
