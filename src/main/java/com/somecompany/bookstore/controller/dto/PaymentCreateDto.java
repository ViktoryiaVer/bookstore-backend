package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.controller.dto.validation.annotation.OrderIdValidation;
import com.somecompany.bookstore.controller.dto.validation.annotation.UserIdValidation;
import com.somecompany.bookstore.model.entity.enums.PaymentMethod;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class PaymentCreateDto {
    private Long id;
    @Digits(integer = 4, fraction = 0, message = "{msg.validation.user.id.not.valid}")
    @UserIdValidation(message = "{msg.error.user.find.by.id}")
    @NotNull(message = "{msg.validation.user.id.null}")
    private Long userId;
    @Digits(integer = 4, fraction = 0, message = "{msg.validation.order.id.not.valid}")
    @OrderIdValidation(message = "{msg.error.order.find.by.id}")
    @NotNull(message = "{msg.validation.order.id.null}")
    private Long orderId;
    @NotNull(message = "{msg.validation.payment.time.null}")
    private LocalDateTime paymentTime;
    @NotNull(message = "{msg.validation.payment.method.empty}")
    private PaymentMethod paymentMethod;
}
