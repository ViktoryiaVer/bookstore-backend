package com.somecompany.bookstore.exception.user;

public class UserAlreadyExistsException extends UserServiceException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
