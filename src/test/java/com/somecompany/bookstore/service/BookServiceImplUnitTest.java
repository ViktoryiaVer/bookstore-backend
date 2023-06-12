package com.somecompany.bookstore.service;

import com.somecompany.bookstore.exception.NotFoundException;
import com.somecompany.bookstore.exception.ObjectAlreadyExistsException;
import com.somecompany.bookstore.exception.ServiceException;
import com.somecompany.bookstore.model.entity.Book;
import com.somecompany.bookstore.model.repository.BookRepository;
import com.somecompany.bookstore.model.repository.OrderRepository;
import com.somecompany.bookstore.service.api.BookService;
import com.somecompany.bookstore.util.TestObjectUtil;
import com.somecompany.bookstore.util.constant.TestObjectConstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
class BookServiceImplUnitTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MessageSource messageSource;
    private BookService bookService;
    private Book bookWithoutId;
    private Book bookWithId;

    @BeforeEach
    public void setup() {
        bookService = new BookServiceImpl(bookRepository, orderRepository, messageSource);
        bookWithoutId = TestObjectUtil.getBookWithoutId();
        bookWithId = TestObjectUtil.getBookWithId();
    }

    @Test
    void whenFindAllBooks_thenReturnBooks() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Book> pageBook = new PageImpl<>(TestObjectUtil.getBookList());

        when(bookRepository.findAll(pageable)).thenReturn(pageBook);
        Page<Book> foundPage = bookService.getAll(pageable);

        assertEquals(pageBook, foundPage);
        verify(bookRepository, times(1)).findAll(pageable);
    }

    @Test
    void whenFindExitingBookById_thenReturnBook() {
        Long bookId = TestObjectConstant.BOOK_ID;
        when(bookRepository.findByIdOrException(bookId)).thenCallRealMethod();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookWithId));
        Book foundBook = bookService.getById(bookId);

        assertEquals(bookWithId, foundBook);
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void whenFindNonExitingBookById_thenThrowException() {
        Long bookId = TestObjectConstant.BOOK_ID;
        when(bookRepository.findByIdOrException(bookId)).thenCallRealMethod();
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.getById(bookId));
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void whenSaveNewBook_thenReturnSavedBook() {
        when(bookRepository.existsByTitle(bookWithoutId.getTitle())).thenReturn(false);
        when(bookRepository.save(bookWithoutId)).thenReturn(bookWithId);

        Book savedBook = bookService.save(bookWithoutId);

        assertEquals(bookWithId, savedBook);
        verify(bookRepository, times(1)).existsByTitle(bookWithoutId.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void whenSaveBookWithExistingTitle_thenThrowException() {
        when(bookRepository.existsByTitle(bookWithoutId.getTitle())).thenReturn(true);

        assertThrows(ObjectAlreadyExistsException.class, () -> bookService.save(bookWithoutId));
        verify(bookRepository, times(1)).existsByTitle(bookWithoutId.getTitle());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void whenUpdateBook_thenReturnUpdatedBook() {
        bookWithId.setTitle("Updated Test");

        when(bookRepository.existsById(bookWithId.getId())).thenReturn(true);
        when(bookRepository.findByTitle(bookWithId.getTitle())).thenReturn(Optional.of(bookWithId));
        when(bookRepository.save(bookWithId)).thenReturn(bookWithId);

        Book updatedBook = bookService.update(bookWithId);

        assertEquals(bookWithId, updatedBook);
        verify(bookRepository, times(1)).existsById(bookWithId.getId());
        verify(bookRepository, times(1)).findByTitle(bookWithId.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void whenUpdateBookWithSameTitleButAnotherId_thenThrowException() {
        Book existingBookWithAnotherId = TestObjectUtil.getBookWithId();
        existingBookWithAnotherId.setId(2L);

        when(bookRepository.existsById(bookWithId.getId())).thenReturn(true);
        when(bookRepository.findByTitle(bookWithId.getTitle())).thenReturn(Optional.of(existingBookWithAnotherId));

        assertThrows(ObjectAlreadyExistsException.class, () -> bookService.update(bookWithId));
        verify(bookRepository, times(1)).existsById(bookWithId.getId());
        verify(bookRepository, times(1)).findByTitle(anyString());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void whenUpdateNonExistingBook_thenThroeException() {
        when(bookRepository.existsById(bookWithId.getId())).thenReturn(false);

        assertThrows(ServiceException.class, () -> bookService.update(bookWithId));
        verify(bookRepository, never()).findByTitle(anyString());
        verify(bookRepository, never()).save(bookWithId);
    }

    @Test
    void whenDeleteBookWithoutOrders_thenBookIsDeleted() {
        Long bookId = TestObjectConstant.BOOK_ID;

        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(orderRepository.existsOrderByOrderDetailsBookId(bookId)).thenReturn(false);
        doNothing().when(bookRepository).deleteById(bookId);

        bookService.deleteById(bookId);
        verify(orderRepository, times(1)).existsOrderByOrderDetailsBookId(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void whenDeleteBookWithOrders_thenThrowException() {
        Long bookId = TestObjectConstant.BOOK_ID;
        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(orderRepository.existsOrderByOrderDetailsBookId(bookId)).thenReturn(true);

        assertThrows(ServiceException.class, () -> bookService.deleteById(bookId));
        verify(orderRepository, times(1)).existsOrderByOrderDetailsBookId(bookId);
        verify(bookRepository, never()).deleteById(bookId);
    }

    @Test
    void whenDeleteNonExistingBook_thenThroeException() {
        Long bookId = TestObjectConstant.BOOK_ID;

        when(bookRepository.existsById(bookId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> bookService.deleteById(bookId));
        verify(orderRepository, never()).existsOrderByOrderDetailsBookId(bookId);
        verify(bookRepository, never()).deleteById(bookId);
    }
}
