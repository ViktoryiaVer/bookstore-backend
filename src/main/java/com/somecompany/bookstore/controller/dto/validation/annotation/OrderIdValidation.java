package com.somecompany.bookstore.controller.dto.validation.annotation;

import com.somecompany.bookstore.controller.dto.validation.OrderIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Constraint(validatedBy = OrderIdValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD, PARAMETER})
@Documented
public @interface OrderIdValidation {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
