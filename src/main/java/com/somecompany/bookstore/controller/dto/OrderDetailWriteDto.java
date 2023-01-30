package com.somecompany.bookstore.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
public class OrderDetailWriteDto {
    private Long id;
    @ToString.Exclude
    @JsonIgnore
    private OrderWriteDto order;
    private Long bookId;
    private BigDecimal bookPrice;
    private Integer bookQuantity;
}
