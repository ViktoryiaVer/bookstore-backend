package com.somecompany.bookstore.exception.book;

import org.hibernate.service.spi.ServiceException;

public class BookServiceException extends ServiceException {
    public BookServiceException(String message) {
        super(message);
    }
}
