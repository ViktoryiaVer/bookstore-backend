package com.somecompany.bookstore.model.repository;

import com.somecompany.bookstore.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, AbstractRepository {
    boolean existsOrderByUserId(Long id);

    boolean existsOrderByOrderDetailsBookId(Long id);
}
