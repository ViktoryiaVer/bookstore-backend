package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.controller.dto.PaymentWriteDto;
import com.somecompany.bookstore.mapper.PaymentMapper;
import com.somecompany.bookstore.controller.dto.PaymentDto;
import com.somecompany.bookstore.mapper.PaymentWriteMapper;
import com.somecompany.bookstore.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/payments/")
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentMapper mapper;
    private final PaymentWriteMapper writeMapper;
    @GetMapping()
    public List<PaymentDto> getAllPayments() {
        return paymentService.getAll().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public PaymentDto getPayment(@PathVariable Long id) {
        return mapper.toDto(paymentService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDto createPayment(@RequestBody PaymentWriteDto payment) {
        return mapper.toDto(paymentService.save(writeMapper.toEntity(payment)));
    }
    @PutMapping()
    public PaymentDto updatePayment(@RequestBody PaymentWriteDto payment) {
        return mapper.toDto(paymentService.update(writeMapper.toEntity(payment)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deletePayment(@PathVariable Long id) {
        paymentService.deleteById(id);
    }
}
