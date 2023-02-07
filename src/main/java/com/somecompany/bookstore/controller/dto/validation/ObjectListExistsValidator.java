package com.somecompany.bookstore.controller.dto.validation;

import com.somecompany.bookstore.controller.dto.validation.annotation.ObjectExistsValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@RequiredArgsConstructor
public class ObjectListExistsValidator implements ConstraintValidator<ObjectExistsValidation, List<Long>> {
    private final ApplicationContext context;
    private JpaRepository<?, Long> repository;

    @Override
    public void initialize(ObjectExistsValidation constraintAnnotation) {
        repository = context.getBean(constraintAnnotation.repository());
    }

    @Override
    public boolean isValid(List<Long> value, ConstraintValidatorContext context) {
        for (Long id : value) {
            if (!repository.existsById(id)) {
                return false;
            }
        }
        return true;
    }
}
