package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.controller.dto.PaymentCreateDto;
import com.somecompany.bookstore.mapper.PaymentCreateMapper;
import com.somecompany.bookstore.mapper.PaymentMapper;
import com.somecompany.bookstore.controller.dto.PaymentDto;
import com.somecompany.bookstore.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/payments/")
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentMapper mapper;
    private final PaymentCreateMapper writeMapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<List<PaymentDto>> getAllPayments(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(paymentService.getAll(pageable).stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<PaymentDto> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(paymentService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PaymentDto> createPayment(@Valid @RequestBody PaymentCreateDto payment) {
        PaymentDto savedPayment = mapper.toDto(paymentService.save(writeMapper.toEntity(payment)));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPayment);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PaymentDto> updatePayment(@Valid @RequestBody PaymentCreateDto payment) {
        PaymentDto updatedPayment = mapper.toDto(paymentService.update(writeMapper.toEntity(payment)));
        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PaymentDto> deletePayment(@PathVariable Long id) {
        paymentService.deleteById(id);
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).build();
    }
}
