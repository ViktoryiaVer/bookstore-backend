package com.somecompany.bookstore.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "dto for returning paginated result for items")
public class ItemsWithPaginationDto<T> {
    @Schema(description = "list of items")
    private List<T> items;
    @Schema(description = "total pages count")
    private Integer totalPages;
}
