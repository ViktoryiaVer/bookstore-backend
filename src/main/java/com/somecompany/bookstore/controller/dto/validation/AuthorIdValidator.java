package com.somecompany.bookstore.controller.dto.validation;

import com.somecompany.bookstore.controller.dto.validation.annotation.AuthorIdValidation;
import com.somecompany.bookstore.service.AuthorService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@RequiredArgsConstructor
public class AuthorIdValidator implements ConstraintValidator<AuthorIdValidation, List<Long>> {
    private final AuthorService authorService;

    @Override
    public boolean isValid(List<Long> value, ConstraintValidatorContext context) {
        for (Long id : value) {
            if (!authorService.existsById(id)) {
                return false;
            }
        }
        return true;
    }
}
