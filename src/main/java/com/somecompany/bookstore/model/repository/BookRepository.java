package com.somecompany.bookstore.model.repository;

import com.somecompany.bookstore.model.entity.Book;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends AbstractRepository<Book> {
    boolean existsByTitle(String title);

    Optional<Book> findByTitle(String title);

    boolean existsBookByAuthorsId(Long id);
}
