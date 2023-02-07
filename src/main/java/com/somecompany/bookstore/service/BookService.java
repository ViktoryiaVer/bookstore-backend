package com.somecompany.bookstore.service;

import com.somecompany.bookstore.exception.NotFoundException;
import com.somecompany.bookstore.exception.ObjectAlreadyExistsException;
import com.somecompany.bookstore.exception.ServiceException;
import com.somecompany.bookstore.model.entity.Book;
import com.somecompany.bookstore.model.repository.BookRepository;
import com.somecompany.bookstore.model.repository.OrderRepository;
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
public class BookService {
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final MessageSource messageSource;

    public Page<Book> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book getById(Long id) {
        return bookRepository.findByIdOrException(id);
    }

    @Transactional
    public Book save(Book book) {
        if (bookRepository.existsByTitle(book.getTitle())) {
            throw new ObjectAlreadyExistsException(messageSource.getMessage("msg.error.book.exists", null,
                    LocaleContextHolder.getLocale()));
        }
        return bookRepository.save(book);
    }

    @Transactional
    public Book update(Book book) {
        if (!bookRepository.existsById(book.getId())) {
            throw new ServiceException(messageSource.getMessage("msg.error.book.update", null,
                    LocaleContextHolder.getLocale()));
        }

        Optional<Book> existingBook = bookRepository.findByTitle(book.getTitle());
        if (existingBook.isPresent() && !Objects.equals(existingBook.get().getId(), book.getId())) {
            throw new ObjectAlreadyExistsException(messageSource.getMessage("msg.error.book.exists", null,
                    LocaleContextHolder.getLocale()));
        }
        return bookRepository.save(book);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException(messageSource.getMessage("msg.error.book.find.by.id", null, LocaleContextHolder.getLocale()));
        }

        if (orderRepository.existsOrderByOrderDetailsBookId(id)) {
            throw new ServiceException(messageSource.getMessage("msg.error.book.delete", null,
                    LocaleContextHolder.getLocale()));
        }

        bookRepository.deleteById(id);
    }
}
