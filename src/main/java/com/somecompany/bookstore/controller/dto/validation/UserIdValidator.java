package com.somecompany.bookstore.controller.dto.validation;

import com.somecompany.bookstore.controller.dto.validation.annotation.UserIdValidation;
import com.somecompany.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UserIdValidator implements ConstraintValidator<UserIdValidation, Long> {
    private final UserService userService;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return userService.existsById(value);
    }
}
