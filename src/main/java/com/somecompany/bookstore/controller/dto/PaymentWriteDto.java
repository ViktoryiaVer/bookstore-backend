package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.controller.dto.enums.PaymentMethodDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentWriteDto {
    private Long id;
    private Long userId;
    private Long orderId;
    private LocalDateTime paymentTime;
    private PaymentMethodDto paymentMethod;
}
