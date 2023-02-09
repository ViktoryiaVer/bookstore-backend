package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.controller.dto.validation.annotation.ObjectExistsValidation;
import com.somecompany.bookstore.model.entity.enums.PaymentMethod;
import com.somecompany.bookstore.model.repository.OrderRepository;
import com.somecompany.bookstore.model.repository.UserRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Schema(description = "dto for create and update operations with payment")
public class PaymentCreateDto {
    @Schema(description = "Id of the payment")
    private Long id;

    @Schema(description = "User id of the payment")
    @NotNull(message = "{msg.validation.user.id.null}")
    @Digits(integer = 4, fraction = 0, message = "{msg.validation.user.id.not.valid}")
    @ObjectExistsValidation(repository = UserRepository.class, message = "{msg.error.user.find.by.id}")
    private Long userId;

    @Schema(description = "Order id of the payment")
    @NotNull(message = "{msg.validation.order.id.null}")
    @Digits(integer = 4, fraction = 0, message = "{msg.validation.order.id.not.valid}")
    @ObjectExistsValidation(repository = OrderRepository.class, message = "{msg.error.order.find.by.id}")
    private Long orderId;

    @Schema(description = "Payment time of the payment")
    @NotNull(message = "{msg.validation.payment.time.null}")
    private LocalDateTime paymentTime;
    @Schema(description = "Payment method of the payment")
    @NotNull(message = "{msg.validation.payment.method.empty}")
    private PaymentMethod paymentMethod;
}
