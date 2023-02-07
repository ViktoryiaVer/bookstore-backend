package com.somecompany.bookstore.model.repository;

import com.somecompany.bookstore.model.entity.Author;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends AbstractRepository<Author> {
    boolean existsByFirstNameAndLastName(String fistName, String lastName);

    Optional<Author> findAuthorByFirstNameAndLastName(String fistName, String lastName);
}
