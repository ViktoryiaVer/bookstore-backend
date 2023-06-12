package com.somecompany.bookstore.service.api;

import com.somecompany.bookstore.model.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    Page<Payment> getAll(Pageable pageable);

    Payment getById(Long id);

    Payment save(Payment payment);

    Payment update(Payment payment);

    void deleteById(Long id);
}
