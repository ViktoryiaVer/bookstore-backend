package com.somecompany.bookstore.controller.dto.validation.annotation;

import com.somecompany.bookstore.controller.dto.validation.BookIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Constraint(validatedBy = BookIdValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD, PARAMETER})
@Documented
public @interface BookIdValidation {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
