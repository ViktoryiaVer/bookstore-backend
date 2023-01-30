package com.somecompany.bookstore.model.repository;

import com.somecompany.bookstore.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
