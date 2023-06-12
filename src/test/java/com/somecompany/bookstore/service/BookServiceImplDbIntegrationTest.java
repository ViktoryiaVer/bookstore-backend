package com.somecompany.bookstore.service;

import com.somecompany.bookstore.exception.NotFoundException;
import com.somecompany.bookstore.exception.ObjectAlreadyExistsException;
import com.somecompany.bookstore.exception.ServiceException;
import com.somecompany.bookstore.model.entity.Book;
import com.somecompany.bookstore.model.repository.BookRepository;
import com.somecompany.bookstore.service.api.BookService;
import com.somecompany.bookstore.util.BookstorePostgresqlContainer;
import com.somecompany.bookstore.util.TestObjectUtil;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BookServiceImplDbIntegrationTest {
    @ClassRule
    public static final PostgreSQLContainer<BookstorePostgresqlContainer> postgreSQLContainer = BookstorePostgresqlContainer.getInstance();
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookService bookService;
    private Book bookWithoutIdToSave;
    private Book existingBook;

    @BeforeEach
    public void setup() {
        bookWithoutIdToSave = TestObjectUtil.getBookWithoutId();
        existingBook = TestObjectUtil.getExistingBook();
    }

    @Test
    void whenFindAllBooksInPage_thenReturnAllBooksInPage() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> foundBook = bookService.getAll(pageable).stream().toList();
        assertEquals(pageable.getPageSize(), foundBook.size());
    }

    @Test
    void whenFindExistingBookById_thenReturnBook() {
        Book foundBook = bookService.getById(existingBook.getId());

        assertEquals(existingBook.getId(), foundBook.getId());
        assertEquals(existingBook.getTitle(), foundBook.getTitle());
        assertEquals(existingBook.getPublisher(), foundBook.getPublisher());
        assertEquals(existingBook.getIsbn(), foundBook.getIsbn());
        assertEquals(existingBook.getYearOfPublication(), foundBook.getYearOfPublication());
        assertEquals(existingBook.getPrice(), foundBook.getPrice());
        assertEquals(existingBook.getAuthors().size(), foundBook.getAuthors().size());
        assertEquals(existingBook.getAuthors().get(0).getId(), foundBook.getAuthors().get(0).getId());
    }

    @Test
    void whenFindNonExistingUserById_thenThrowException() {
        Long nonExistingId = 100L;
        assertThrows(NotFoundException.class, () -> bookService.getById(nonExistingId));
    }

    @Test
    void whenSaveOneNewBook_thenReturnSavedBook() {
        int listSizeBeforeSaving = getAllBooksListSize();
        Book savedBook = bookService.save(bookWithoutIdToSave);

        assertNotNull(savedBook.getId());
        assertEquals(bookWithoutIdToSave.getTitle(), savedBook.getTitle());
        assertEquals(bookWithoutIdToSave.getPublisher(), savedBook.getPublisher());
        assertEquals(bookWithoutIdToSave.getIsbn(), savedBook.getIsbn());
        assertEquals(bookWithoutIdToSave.getYearOfPublication(), savedBook.getYearOfPublication());
        assertEquals(bookWithoutIdToSave.getPrice(), savedBook.getPrice());
        assertEquals(bookWithoutIdToSave.getAuthors().size(), savedBook.getAuthors().size());
        assertEquals(bookWithoutIdToSave.getAuthors().get(0).getId(), savedBook.getAuthors().get(0).getId());
        assertEquals(listSizeBeforeSaving + 1, bookRepository.findAll().size());

        bookRepository.delete(savedBook);
    }

    @Test
    void whenSaveBookWithExistingTitle_thenThrowException() {
        int listSizeBeforeSaving = getAllBooksListSize();
        bookWithoutIdToSave.setTitle(existingBook.getTitle());

        assertThrows(ObjectAlreadyExistsException.class, () -> bookService.save(bookWithoutIdToSave));
        assertEquals(listSizeBeforeSaving, getAllBooksListSize());
    }

    @Test
    void whenUpdateBook_thenReturnUpdatedBook() {
        int listSizeBeforeUpdating = getAllBooksListSize();
        existingBook.setTitle("Updated-Title");
        Book updatedBook = bookService.update(existingBook);

        assertEquals(existingBook.getId(), updatedBook.getId());
        assertEquals(existingBook.getTitle(), updatedBook.getTitle());
        assertEquals(existingBook.getPublisher(), updatedBook.getPublisher());
        assertEquals(existingBook.getIsbn(), updatedBook.getIsbn());
        assertEquals(existingBook.getYearOfPublication(), updatedBook.getYearOfPublication());
        assertEquals(existingBook.getPrice(), updatedBook.getPrice());
        assertEquals(existingBook.getAuthors().size(), updatedBook.getAuthors().size());
        assertEquals(existingBook.getAuthors().get(0).getId(), updatedBook.getAuthors().get(0).getId());
        assertEquals(listSizeBeforeUpdating, getAllBooksListSize());

        existingBook.setTitle(TestObjectUtil.getExistingBook().getTitle());
        bookRepository.saveAndFlush(existingBook);
    }

    @Test
    void whenUpdateBookWithExistingTitleButAnotherId_thenThrowException() {
        existingBook.setId(16L);
        existingBook.setPublisher("Updated-Publisher");

        assertThrows(ObjectAlreadyExistsException.class, () -> bookService.update(existingBook));
    }

    @Test
    void whenUpdateNonExistingBook_thenThrowException() {
        existingBook.setId(100L);
        assertThrows(ServiceException.class, () -> bookService.update(existingBook));
    }

    @Test
    void whenDeleteBookWithoutOrders_thenBookIsDeleted() {
        Book savedBook = bookRepository.saveAndFlush(bookWithoutIdToSave);
        int listSizeBeforeDeleting = getAllBooksListSize();

        bookService.deleteById(savedBook.getId());
        assertEquals(listSizeBeforeDeleting - 1, getAllBooksListSize());
    }

    @Test
    void whenDeleteBookWithOrders_thenThrowException() {
        Long bookWithOrdersId = 17L;
        int listSizeBeforeDeleting = getAllBooksListSize();

        assertThrows(ServiceException.class, () -> bookService.deleteById(bookWithOrdersId));
        assertEquals(listSizeBeforeDeleting, getAllBooksListSize());
    }

    @Test
    void whenDeleteNonExistingBook_thenThrowException() {
        Long nonExistingBookId = 100L;
        int listSizeBeforeDeleting = getAllBooksListSize();

        assertThrows(NotFoundException.class, () -> bookService.deleteById(nonExistingBookId));
        assertEquals(listSizeBeforeDeleting, getAllBooksListSize());
    }

    private int getAllBooksListSize() {
        return bookRepository.findAll().size();
    }
}
