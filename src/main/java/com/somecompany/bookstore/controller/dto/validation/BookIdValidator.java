package com.somecompany.bookstore.controller.dto.validation;

import com.somecompany.bookstore.controller.dto.validation.annotation.BookIdValidation;
import com.somecompany.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class BookIdValidator implements ConstraintValidator<BookIdValidation, Long> {
    private final BookService bookService;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return bookService.existsById(value);
    }
}
