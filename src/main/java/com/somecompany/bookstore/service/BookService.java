package com.somecompany.bookstore.service;

import com.somecompany.bookstore.model.entity.Book;
import com.somecompany.bookstore.model.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    public List<Book> getAll() {
        return bookRepository.findAll();
    }
    public Book getById(Long id) {
        return bookRepository.findById(id).orElseThrow(RuntimeException::new);
    }
    public Book save(Book book) {
        return bookRepository.save(book);
    }
    public Book update(Book book) {
        return bookRepository.save(book);
    }
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
