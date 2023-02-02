package com.somecompany.bookstore.exception.user;

import com.somecompany.bookstore.exception.ServiceException;

public class UserServiceException extends ServiceException {
    public UserServiceException(String message) {
        super(message);
    }
}
