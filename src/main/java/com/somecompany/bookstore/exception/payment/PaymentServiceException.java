package com.somecompany.bookstore.exception.payment;

import com.somecompany.bookstore.exception.ServiceException;

public class PaymentServiceException extends ServiceException {

    public PaymentServiceException(String message) {
        super(message);
    }
}
