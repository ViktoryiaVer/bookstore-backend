package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.controller.dto.validation.annotation.AuthorIdValidation;
import com.somecompany.bookstore.model.entity.enums.Cover;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BookCreateDto {
    private Long id;
    @NotBlank(message = "{msg.validation.title.empty}")
    private String title;
    @NotBlank(message = "{msg.validation.publisher.empty}")
    private String publisher;
    @NotBlank(message = "{msg.validation.isbn.empty}")
    private String isbn;
    @Digits(integer = 4, fraction = 0, message = "{msg.validation.publication.year.not.valid}")
    @Min(value = 1800, message = "{msg.validation.publication.year.not.valid}")
    @Max(value = 2100, message = "{msg.validation.publication.year.not.valid}")
    @NotNull(message = "{msg.validation.publication.year.empty}")
    private Integer yearOfPublication;
    @DecimalMax(value = "10000.00", inclusive = false, message = "{msg.validation.price.max}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{msg.validation.price.min}")
    @Digits(integer = 6, fraction = 2, message = "{msg.validation.price.not.valid}")
    @NotNull(message = "{msg.validation.price.empty}")
    private BigDecimal price;
    @NotNull(message = "{msg.validation.cover.empty}")
    private Cover cover;
    @AuthorIdValidation(message = "{msg.error.author.find.by.id}")
    @NotNull(message = "{msg.validation.authors.id.null}")
    private List<Long> authorIds;
}
