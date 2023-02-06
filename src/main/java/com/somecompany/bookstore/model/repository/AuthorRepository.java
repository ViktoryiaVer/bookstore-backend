package com.somecompany.bookstore.model.repository;

import com.somecompany.bookstore.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, AbstractRepository {
    boolean existsByFirstNameAndLastName(String fistName, String lastName);

    Optional<Author> findAuthorByFirstNameAndLastName(String fistName, String lastName);
}
