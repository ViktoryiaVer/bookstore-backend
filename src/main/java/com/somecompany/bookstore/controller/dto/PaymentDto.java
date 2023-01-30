package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.controller.dto.enums.PaymentMethodDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDto {
    private Long id;
    private UserDto user;
    private OrderDto order;
    private LocalDateTime paymentTime;
    private PaymentMethodDto paymentMethod;
}
