package com.somecompany.bookstore.service;

import com.somecompany.bookstore.model.entity.Author;
import com.somecompany.bookstore.model.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    public List<Author> getAll() {
        return authorRepository.findAll();
    }
    public Author getById(Long id) {
        return authorRepository.findById(id).orElseThrow(RuntimeException::new);
    }
    public Author save(Author author) {
        return authorRepository.save(author);
    }
    public Author update(Author author) {
        return authorRepository.save(author);
    }
    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }
}
