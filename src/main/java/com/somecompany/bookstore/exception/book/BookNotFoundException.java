package com.somecompany.bookstore.exception.book;

import com.somecompany.bookstore.exception.NotFoundException;

public class BookNotFoundException extends NotFoundException {
    public BookNotFoundException(String message) {
        super(message);
    }
}
