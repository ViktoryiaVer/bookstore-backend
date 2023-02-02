package com.somecompany.bookstore.exception.author;

import com.somecompany.bookstore.exception.NotFoundException;

public class AuthorNotFoundException extends NotFoundException {
    public AuthorNotFoundException(String message) {
        super(message);
    }
}
