package com.somecompany.bookstore.service.api;

import com.somecompany.bookstore.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Page<Book> getAll(Pageable pageable);

    Book getById(Long id);

    Book save(Book book);

    Book update(Book book);

    void deleteById(Long id);
}
