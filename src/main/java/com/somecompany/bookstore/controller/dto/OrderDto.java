package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.controller.dto.enums.OrderStatusDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private UserDto user;
    private BigDecimal totalCost;
    private OrderStatusDto status;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<OrderDetailDto> orderDetails = new ArrayList<>();

    public void addOrderDetailDto(OrderDetailDto detail) {
        orderDetails.add(detail);
        detail.setOrder(this);
    }
}
