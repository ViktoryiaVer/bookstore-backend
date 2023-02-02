package com.somecompany.bookstore.exception.order;

import com.somecompany.bookstore.exception.NotFoundException;

public class OrderNotFoundException extends NotFoundException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
