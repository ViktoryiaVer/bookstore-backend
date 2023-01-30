package com.somecompany.bookstore.model.repository;

import com.somecompany.bookstore.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
