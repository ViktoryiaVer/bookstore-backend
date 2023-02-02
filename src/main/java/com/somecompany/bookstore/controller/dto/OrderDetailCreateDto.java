package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.controller.dto.validation.annotation.BookIdValidation;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class OrderDetailCreateDto {
    private Long id;
    @Digits(integer = 4, fraction = 0, message = "{msg.validation.book.id.not.valid}")
    @BookIdValidation(message = "{msg.error.book.find.by.id}")
    @NotNull(message = "{msg.validation.book.id.null}")
    private Long bookId;
    @DecimalMax(value = "10000.00", inclusive = false, message = "{msg.validation.price.max}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{msg.validation.price.min}")
    @Digits(integer = 6, fraction = 2, message = "{msg.validation.price.not.valid}")
    @NotNull(message = "{msg.validation.price.empty}")
    private BigDecimal bookPrice;
    @Digits(integer = 4, fraction = 0, message = "{msg.validation.book.quantity.not.valid}")
    @Min(value = 1, message = "{msg.validation.book.quantity.zero}")
    @Max(value = 99, message = "{msg.validation.book.quantity.max}")
    @NotNull(message = "{msg.validation.book.quantity.empty}")
    private Integer bookQuantity;
}
