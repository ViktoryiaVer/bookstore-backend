package com.somecompany.bookstore.service;

import com.somecompany.bookstore.model.entity.Payment;
import com.somecompany.bookstore.model.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    public Payment getById(Long id) {
        return paymentRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment update(Payment payment) {
        return paymentRepository.save(payment);
    }

    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }
}
