package com.somecompany.bookstore.model.repository;

import com.somecompany.bookstore.constant.QueryConstant;
import com.somecompany.bookstore.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByFirstNameAndLastName(String fistName, String lastName);

    Optional<Author> findAuthorByFirstNameAndLastName(String fistName, String lastName);

    @Query(nativeQuery = true, value = QueryConstant.SQL_CHECK_IF_AUTHOR_HAS_BOOK)
    boolean checkIfAuthorHasABook(Long id);
}
