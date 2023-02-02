package com.somecompany.bookstore.exception.payment;

public class PaymentAlreadyExistsException extends PaymentServiceException {
    public PaymentAlreadyExistsException(String message) {
        super(message);
    }
}
