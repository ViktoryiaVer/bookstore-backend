package com.somecompany.bookstore.controller.dto.response;

import com.somecompany.bookstore.controller.dto.BookDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "dto for returning paginated result for books")
public class BooksWithPaginationDto {
    private List<BookDto> books;
    private Integer totalPages;
}
