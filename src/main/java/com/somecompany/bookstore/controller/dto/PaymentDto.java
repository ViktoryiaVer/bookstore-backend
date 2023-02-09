package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.model.entity.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Schema(description = "dto for read operations with payment")
public class PaymentDto {
    @Schema(description = "Id of the payment")
    private Long id;
    @Valid
    @NotNull(message = "{msg.validation.user.null}")
    private UserDto user;
    @Valid
    @NotNull(message = "{msg.validation.order.null}")
    private OrderDto order;
    @Schema(description = "Payment time of the payment")
    @NotNull(message = "{msg.validation.payment.time.null}")
    private LocalDateTime paymentTime;
    @Schema(description = "Payment method of the payment")
    @NotNull(message = "{msg.validation.payment.method.empty}")
    private PaymentMethod paymentMethod;
}
