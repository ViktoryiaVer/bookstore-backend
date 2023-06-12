package com.somecompany.bookstore.mapper;

import com.somecompany.bookstore.controller.dto.PaymentCreateDto;
import com.somecompany.bookstore.model.entity.Order;
import com.somecompany.bookstore.model.entity.Payment;
import com.somecompany.bookstore.model.entity.User;
import com.somecompany.bookstore.service.api.OrderService;
import com.somecompany.bookstore.service.api.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = {UserMapper.class})
public abstract class PaymentCreateMapper {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "orderId", target = "order")
    public abstract Payment toEntity(PaymentCreateDto paymentWriteDto);

    protected User getUserById(Long id) {
        return userService.getById(id);
    }

    protected Order getOrderById(Long id) {
        return orderService.getById(id);
    }
}
