package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.controller.dto.enums.CoverDto;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class BookDto {
    private Long id;
    private String title;
    private String publisher;
    private String isbn;
    private Integer yearOfPublication;
    private BigDecimal price;
    private CoverDto cover;
    private List<AuthorDto> authors = new ArrayList<>();
}
