package com.somecompany.bookstore.exception.payment;

import com.somecompany.bookstore.exception.NotFoundException;

public class PaymentNotFoundException extends NotFoundException {
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
