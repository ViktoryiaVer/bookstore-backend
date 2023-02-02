package com.somecompany.bookstore.exception.order;

import com.somecompany.bookstore.exception.ServiceException;

public class OrderServiceException extends ServiceException {
    public OrderServiceException(String message) {
        super(message);
    }
}
