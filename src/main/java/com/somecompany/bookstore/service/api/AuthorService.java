package com.somecompany.bookstore.service.api;

import com.somecompany.bookstore.model.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {

    Page<Author> getAll(Pageable pageable);

    Author getById(Long id);

    Author save(Author author);

    Author update(Author author);

    void deleteById(Long id);
}
