package com.somecompany.bookstore.model.repository;

import com.somecompany.bookstore.model.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends AbstractRepository<Order> {
    boolean existsOrderByUserId(Long id);

    boolean existsOrderByOrderDetailsBookId(Long id);
}
