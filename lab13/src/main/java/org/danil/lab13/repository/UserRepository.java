package org.danil.lab13.repository;

import org.danil.lab13.model.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    @Query("select * from users where firstname || ' ' || lastname ilike CONCAT('%', :name, '%')")
    Flux<User> findAllByName(@Param("name") String name);
}