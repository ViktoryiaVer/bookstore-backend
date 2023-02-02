package com.somecompany.bookstore.controller.dto.validation;

import com.somecompany.bookstore.controller.dto.validation.annotation.OrderIdValidation;
import com.somecompany.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class OrderIdValidator implements ConstraintValidator<OrderIdValidation, Long> {
    private final OrderService orderService;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return orderService.existsById(value);
    }
}
