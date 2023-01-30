package com.somecompany.bookstore.model.repository;

import com.somecompany.bookstore.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
