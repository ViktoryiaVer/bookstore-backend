package com.somecompany.bookstore.exception;

import lombok.Getter;
import org.springframework.validation.Errors;

public class ValidationException extends RuntimeException {
    @Getter
    private final Errors errors;

    public ValidationException(Errors errors) {
        this.errors = errors;
    }
}
