package com.somecompany.bookstore.mapper;

import com.somecompany.bookstore.controller.dto.PaymentWriteDto;
import com.somecompany.bookstore.model.entity.Order;
import com.somecompany.bookstore.model.entity.Payment;
import com.somecompany.bookstore.model.entity.User;
import com.somecompany.bookstore.service.OrderService;
import com.somecompany.bookstore.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class PaymentWriteMapper {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "orderId", target = "order")
    public abstract Payment toEntity(PaymentWriteDto paymentWriteDto);

    protected User getUserById(Long id) {
        return userService.getById(id);
    }

    protected Order getOrderById(Long id) {
        return orderService.getById(id);
    }
}
