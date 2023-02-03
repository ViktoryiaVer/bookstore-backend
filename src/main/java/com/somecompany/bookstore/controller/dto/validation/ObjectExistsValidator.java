package com.somecompany.bookstore.controller.dto.validation;

import com.somecompany.bookstore.controller.dto.validation.annotation.ObjectExistsValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class ObjectExistsValidator implements ConstraintValidator<ObjectExistsValidation, Long> {
    private final ApplicationContext context;
    private JpaRepository<?, Long> repository;

    @Override
    public void initialize(ObjectExistsValidation constraintAnnotation) {
        repository = context.getBean(constraintAnnotation.repository());
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return repository.existsById(value);
    }
}
