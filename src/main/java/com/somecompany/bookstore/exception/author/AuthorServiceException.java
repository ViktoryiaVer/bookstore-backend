package com.somecompany.bookstore.exception.author;

import com.somecompany.bookstore.exception.ServiceException;

public class AuthorServiceException extends ServiceException {
    public AuthorServiceException(String message) {
        super(message);
    }
}
