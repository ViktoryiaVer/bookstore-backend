package com.somecompany.bookstore.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.somecompany.bookstore.model.entity.Book;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
public class OrderDetailDto {
    private Long id;
    @ToString.Exclude
    @JsonIgnore
    private OrderDto order;
    private Book book;
    private BigDecimal bookPrice;
    private Integer bookQuantity;
}
