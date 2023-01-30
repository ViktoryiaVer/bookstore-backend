package com.somecompany.bookstore.model.repository;

import com.somecompany.bookstore.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
