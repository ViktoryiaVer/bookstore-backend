package com.somecompany.bookstore.exception.book;

public class BookAlreadyExistsException extends BookServiceException {
    public BookAlreadyExistsException(String message) {
        super(message);
    }
}
