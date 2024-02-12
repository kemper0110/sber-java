package org.danil.lab13.repository;

import org.danil.lab13.model.Request;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends ReactiveCrudRepository<Request, Long> {
}