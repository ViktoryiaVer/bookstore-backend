package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.controller.dto.enums.OrderStatusDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderWriteDto {
    private Long id;
    private Long userId;
    private BigDecimal totalCost;
    private OrderStatusDto status;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<OrderDetailWriteDto> orderDetails = new ArrayList<>();

    public void addOrderDetailDto(OrderDetailWriteDto detail) {
        orderDetails.add(detail);
        detail.setOrder(this);
    }
}
