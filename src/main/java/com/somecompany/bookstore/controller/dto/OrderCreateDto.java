package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.controller.dto.validation.annotation.ObjectExistsValidation;
import com.somecompany.bookstore.model.entity.enums.OrderStatus;
import com.somecompany.bookstore.model.repository.UserRepository;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderCreateDto {
    private Long id;
    @NotNull(message = "{msg.validation.user.id.null}")
    @Digits(integer = 4, fraction = 0, message = "{msg.validation.user.id.not.valid}")
    @ObjectExistsValidation(repository = UserRepository.class, message = "{msg.error.user.find.by.id}")
    private Long userId;
    @NotNull(message = "{msg.validation.total.cost.empty}")
    @Digits(integer = 6, fraction = 2, message = "{msg.validation.total.cost.not.valid}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{msg.validation.total.cost.min}")
    @DecimalMax(value = "10000.00", inclusive = false, message = "{msg.validation.total.cost.max}")
    private BigDecimal totalCost;
    @NotNull(message = "{msg.validation.order.status.empty}")
    private OrderStatus status;
    @Valid
    @NotNull(message = "{msg.validation.order.detail.null}")
    private List<OrderDetailCreateDto> orderDetails = new ArrayList<>();
}
