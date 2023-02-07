package com.somecompany.bookstore.model.repository;

import com.somecompany.bookstore.model.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends AbstractRepository<User> {
    @Override
    @EntityGraph("Person.withLogin")
    List<User> findAll();

    boolean existsByEmail(String email);

    boolean existsByLoginUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByLoginUsername(String username);
}
