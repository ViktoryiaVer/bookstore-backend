package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.controller.dto.response.MessageDto;
import com.somecompany.bookstore.controller.dto.response.ValidationResultDto;
import com.somecompany.bookstore.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<MessageDto> handIleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<MessageDto> handIleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDto(e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {PropertyReferenceException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<MessageDto> handIleBadRequest(RuntimeException e) {
        return ResponseEntity.badRequest().body(new MessageDto(e.getMessage()));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationResultDto> handleValidationError(MethodArgumentNotValidException e) {
        Map<String, List<String>> errors = mapErrors(e.getAllErrors());
        return ResponseEntity.badRequest().body(new ValidationResultDto(errors));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<MessageDto> handIleRuntimeException(RuntimeException e) {
        return ResponseEntity.internalServerError().body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<MessageDto> handIleCheckedException(Exception e) {
        return ResponseEntity.internalServerError().body(new MessageDto(messageSource.getMessage("msg.checked.exception.message", null,
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
