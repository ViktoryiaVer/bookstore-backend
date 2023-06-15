package com.somecompany.bookstore.service;

import com.querydsl.core.BooleanBuilder;
import com.somecompany.bookstore.aspect.logging.annotation.LogInvocation;
import com.somecompany.bookstore.controller.dto.AuthorFilterDto;
import com.somecompany.bookstore.exception.NotFoundException;
import com.somecompany.bookstore.exception.ObjectAlreadyExistsException;
import com.somecompany.bookstore.exception.ServiceException;
import com.somecompany.bookstore.model.entity.Author;
import com.somecompany.bookstore.model.entity.QAuthor;
import com.somecompany.bookstore.model.repository.AuthorRepository;
import com.somecompany.bookstore.model.repository.BookRepository;
import com.somecompany.bookstore.service.api.AuthorService;
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
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final MessageSource messageSource;

    @Override
    @LogInvocation
    public Page<Author> getAll(AuthorFilterDto authorFilterDto, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (authorFilterDto.getLastName() != null) {
            builder.and(QAuthor.author.lastName.containsIgnoreCase(authorFilterDto.getLastName()));
        }
        return authorRepository.findAll(builder, pageable);
    }

    @Override
    @LogInvocation
    public Author getById(Long id) {
        return authorRepository.findByIdOrException(id);
    }

    @Override
    @LogInvocation
    @Transactional
    public Author save(Author author) {
        if (authorRepository.existsByFirstNameAndLastName(author.getFirstName(), author.getLastName())) {
            throw new ObjectAlreadyExistsException(messageSource.getMessage("msg.error.author.exists", null,
                    LocaleContextHolder.getLocale()));
        }
        return authorRepository.save(author);
    }

    @Override
    @LogInvocation
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

    @Override
    @LogInvocation
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
