package org.danil.lab13.repository;

import org.danil.lab13.model.Response;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseRepository extends ReactiveCrudRepository<Response, Long> {
}