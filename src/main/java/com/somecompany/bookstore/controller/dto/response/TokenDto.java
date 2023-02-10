package com.somecompany.bookstore.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "dto for returning a token")
public class TokenDto {
    @Schema(description = "Token value")
    private String token;
}
