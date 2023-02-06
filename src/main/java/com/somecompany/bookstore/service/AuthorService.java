package com.somecompany.bookstore.service;

import com.somecompany.bookstore.exception.NotFoundException;
import com.somecompany.bookstore.exception.ObjectAlreadyExistsException;
import com.somecompany.bookstore.exception.ServiceException;
import com.somecompany.bookstore.model.entity.Author;
import com.somecompany.bookstore.model.repository.AuthorRepository;
import com.somecompany.bookstore.model.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final MessageSource messageSource;

    public Page<Author> getAll(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    public Author getById(Long id) {
        return authorRepository.findByIdOrException(authorRepository, id);
    }

    @Transactional
    public Author save(Author author) {
        if (authorRepository.existsByFirstNameAndLastName(author.getFirstName(), author.getLastName())) {
            throw new ObjectAlreadyExistsException(messageSource.getMessage("msg.error.author.exists", null,
                    LocaleContextHolder.getLocale()));
        }
        return authorRepository.save(author);
    }

    @Transactional
    public Author update(Author author) {
        if (!authorRepository.existsById(author.getId())) {
            throw new ServiceException(messageSource.getMessage("msg.error.author.update", null,
                    LocaleContextHolder.getLocale()));
        }

        Optional<Author> existingAuthor = authorRepository.findAuthorByFirstNameAndLastName(author.getFirstName(), author.getLastName());
        if (existingAuthor.isPresent() && !Objects.equals(existingAuthor.get().getId(), author.getId())) {
            throw new ObjectAlreadyExistsException(messageSource.getMessage("msg.error.author.exists", null,
                    LocaleContextHolder.getLocale()));
        }
        return authorRepository.save(author);
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<Author> existingAuthor = authorRepository.findById(id);
        if (existingAuthor.isEmpty()) {
            throw new NotFoundException(messageSource.getMessage("msg.error.author.find.by.id", null,
                    LocaleContextHolder.getLocale()));
        }

        if (bookRepository.existsBookByAuthorsId(id)) {
            throw new ServiceException(messageSource.getMessage("msg.error.author.delete", null,
                    LocaleContextHolder.getLocale()));
        }

        authorRepository.deleteById(id);
    }
}
