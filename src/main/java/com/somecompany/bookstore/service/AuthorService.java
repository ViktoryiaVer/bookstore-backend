package com.somecompany.bookstore.service;

import com.somecompany.bookstore.exception.author.AuthorAlreadyExistsException;
import com.somecompany.bookstore.exception.author.AuthorNotFoundException;
import com.somecompany.bookstore.exception.author.AuthorServiceException;
import com.somecompany.bookstore.model.entity.Author;
import com.somecompany.bookstore.model.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final MessageSource messageSource;

    public Page<Author> getAll(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    public Author getById(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> {
            throw new AuthorNotFoundException(messageSource.getMessage("msg.error.author.find.by.id", null,
                    LocaleContextHolder.getLocale()));
        });
    }

    @Transactional
    public Author save(Author author) {
        if (authorRepository.existsByFirstNameAndLastName(author.getFirstName(), author.getLastName())) {
            throw new AuthorAlreadyExistsException(messageSource.getMessage("msg.error.author.exists", null,
                    LocaleContextHolder.getLocale()));
        }
        return authorRepository.save(author);
    }

    @Transactional
    public Author update(Author author) {
        if (!authorRepository.existsById(author.getId())) {
            throw new AuthorServiceException(messageSource.getMessage("msg.error.author.update", null,
                    LocaleContextHolder.getLocale()));
        }

        Optional<Author> existingAuthor = authorRepository.findAuthorByFirstNameAndLastName(author.getFirstName(), author.getLastName());
        if (existingAuthor.isPresent() && !Objects.equals(existingAuthor.get().getId(), author.getId())) {
            throw new AuthorAlreadyExistsException(messageSource.getMessage("msg.error.author.exists", null,
                    LocaleContextHolder.getLocale()));
        }
        return authorRepository.save(author);
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<Author> existingAuthor = authorRepository.findById(id);
        if (existingAuthor.isEmpty()) {
            throw new AuthorNotFoundException(messageSource.getMessage("msg.error.author.find.by.id", null,
                    LocaleContextHolder.getLocale()));
        }

        if (authorRepository.checkIfAuthorHasABook(id)) {
            throw new AuthorServiceException(messageSource.getMessage("msg.error.author.delete", null,
                    LocaleContextHolder.getLocale()));
        }

        authorRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return authorRepository.existsById(id);
    }
}
