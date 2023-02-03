package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.model.entity.Book;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class OrderDetailDto {
    private Long id;
    @Valid
    @NotNull(message = "{msg.validation.book.null}")
    private Book book;
    @NotNull(message = "{msg.validation.price.empty}")
    @Digits(integer = 6, fraction = 2, message = "{msg.validation.price.not.valid}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{msg.validation.price.min}")
    @DecimalMax(value = "10000.00", inclusive = false, message = "{msg.validation.price.max}")
    private BigDecimal bookPrice;
    @NotNull(message = "{msg.validation.book.quantity.empty}")
    @Min(value = 1, message = "{msg.validation.book.quantity.zero}")
    @Max(value = 99, message = "{msg.validation.book.quantity.max}")
    @Digits(integer = 4, fraction = 0, message = "{msg.validation.book.quantity.not.valid}")
    private Integer bookQuantity;
}
