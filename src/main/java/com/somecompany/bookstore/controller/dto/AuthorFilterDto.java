package com.somecompany.bookstore.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Data
@Schema(description = "dto for filtering of authors")
public class AuthorFilterDto {
    @Schema(description = "lastName value to filter")
    String lastName;
}
