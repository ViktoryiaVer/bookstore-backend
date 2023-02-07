package com.somecompany.bookstore.model.repository;

import com.somecompany.bookstore.model.entity.Order;
import com.somecompany.bookstore.model.entity.Payment;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends AbstractRepository<Payment> {
    boolean existsPaymentByOrder(Order order);

    boolean existsByOrderIdAndUserId(Long orderId, Long userId);

    Optional<Payment> findByOrderIdAndUserId(Long orderId, Long userId);
}
