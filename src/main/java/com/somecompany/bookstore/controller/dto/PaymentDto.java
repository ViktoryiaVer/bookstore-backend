package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.model.entity.enums.PaymentMethod;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class PaymentDto {
    private Long id;
    @NotNull(message = "{msg.validation.user.null}")
    @Valid
    private UserDto user;
    @NotNull(message = "{msg.validation.order.null}")
    @Valid
    private OrderDto order;
    @NotNull(message = "{msg.validation.payment.time.null}")
    private LocalDateTime paymentTime;
    @NotNull(message = "{msg.validation.payment.method.empty}")
    private PaymentMethod paymentMethod;
}
