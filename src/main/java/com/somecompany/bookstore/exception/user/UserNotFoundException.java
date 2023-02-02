package com.somecompany.bookstore.exception.user;

import com.somecompany.bookstore.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
