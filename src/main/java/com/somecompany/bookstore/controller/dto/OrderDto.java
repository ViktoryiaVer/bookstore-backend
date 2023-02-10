package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.model.entity.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "dto for read operations with order")
public class OrderDto {
    @Schema(description = "Id of the order")
    private Long id;
    @Valid
    @NotNull(message = "{msg.validation.user.null}")
    private UserDto user;

    @Schema(description = "Total cost of the order")
    @NotNull(message = "{msg.validation.total.cost.empty}")
    @Digits(integer = 6, fraction = 2, message = "{msg.validation.total.cost.not.valid}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{msg.validation.total.cost.min}")
    @DecimalMax(value = "10000.00", inclusive = false, message = "{msg.validation.total.cost.max}")
    private BigDecimal totalCost;

    @Schema(description = "Status of the order")
    @NotNull(message = "{msg.validation.order.status.empty}")
    private OrderStatus status;

    @Valid
    @Schema(description = "List of the order details")
    @NotNull(message = "{msg.validation.order.detail.null}")
    private List<OrderDetailDto> orderDetails = new ArrayList<>();

    public void addOrderDetailDto(OrderDetailDto detail) {
        orderDetails.add(detail);
    }
}
