package com.somecompany.bookstore.exception.author;

public class AuthorAlreadyExistsException extends AuthorServiceException {
    public AuthorAlreadyExistsException(String message) {
        super(message);
    }
}
