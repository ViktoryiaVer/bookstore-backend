package com.somecompany.bookstore.controller.dto.validation.annotation;

import com.somecompany.bookstore.controller.dto.validation.ObjectExistsValidator;
import com.somecompany.bookstore.controller.dto.validation.ObjectListExistsValidator;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Documented
@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ObjectListExistsValidator.class, ObjectExistsValidator.class})
public @interface ObjectExistsValidation {
    Class<? extends JpaRepository<?, Long>> repository();
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
