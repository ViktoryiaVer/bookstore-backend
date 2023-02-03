package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.controller.dto.error.ErrorDto;
import com.somecompany.bookstore.controller.dto.error.ValidationResultDto;
import com.somecompany.bookstore.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {
    private final MessageSource messageSource;

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handIleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(value = {PropertyReferenceException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorDto> handIleBadRequest(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }


    @ExceptionHandler
    public ResponseEntity<ValidationResultDto> handleValidationError(MethodArgumentNotValidException e) {
        Map<String, List<String>> errors = mapErrors(e.getAllErrors());
        return ResponseEntity.badRequest().body(new ValidationResultDto(errors));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handIleRuntimeException(RuntimeException e) {
        return ResponseEntity.internalServerError().body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handIleCheckedException(Exception e) {
        return ResponseEntity.internalServerError().body(new ErrorDto(messageSource.getMessage("msg.checked.exception.message", null,
                LocaleContextHolder.getLocale())));
    }

    private Map<String, List<String>> mapErrors(List<ObjectError> rawErrors) {
        return rawErrors
                .stream()
                .collect(
                        Collectors.groupingBy(
                                ObjectError::getObjectName,
                                Collectors.mapping(ObjectError::getDefaultMessage, Collectors.toList())
                        )
                );
    }
}
