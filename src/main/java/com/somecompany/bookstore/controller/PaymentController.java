package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.controller.dto.PaymentCreateDto;
import com.somecompany.bookstore.exception.ValidationException;
import com.somecompany.bookstore.mapper.PaymentMapper;
import com.somecompany.bookstore.controller.dto.PaymentDto;
import com.somecompany.bookstore.mapper.PaymentWriteMapper;
import com.somecompany.bookstore.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("api/payments/")
@RequiredArgsConstructor
@RestController
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentMapper mapper;
    private final PaymentWriteMapper writeMapper;

    @GetMapping
    public List<PaymentDto> getAllPayments(Pageable pageable) {
        return paymentService.getAll(pageable).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public PaymentDto getPayment(@PathVariable Long id) {
        return mapper.toDto(paymentService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@Valid @RequestBody PaymentCreateDto payment, Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }

        PaymentDto savedPayment = mapper.toDto(paymentService.save(writeMapper.toEntity(payment)));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPayment);
    }

    @PutMapping
    public ResponseEntity<PaymentDto> updatePayment(@Valid @RequestBody PaymentCreateDto payment, Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }

        PaymentDto updatedPayment = mapper.toDto(paymentService.update(writeMapper.toEntity(payment)));
        return ResponseEntity.ok(updatedPayment);
    }

    @ResponseStatus(HttpStatus.RESET_CONTENT)
    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable Long id) {
        paymentService.deleteById(id);
    }
}
