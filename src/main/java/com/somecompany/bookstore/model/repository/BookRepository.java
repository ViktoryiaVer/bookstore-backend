package com.somecompany.bookstore.model.repository;

import com.somecompany.bookstore.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitle(String title);

    Optional<Book> findByTitle(String title);
}
