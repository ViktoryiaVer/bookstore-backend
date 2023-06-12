package com.somecompany.bookstore.service.api;

import com.somecompany.bookstore.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<Order> getAll(Pageable pageable);

    Order getById(Long id);

    Order save(Order order);

    Order update(Order order);

    void deleteById(Long id);
}
