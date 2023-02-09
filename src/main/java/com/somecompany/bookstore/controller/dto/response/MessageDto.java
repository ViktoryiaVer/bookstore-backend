package com.somecompany.bookstore.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "dto for returning a message (both success and failure)")
public class MessageDto {
    @Schema(description = "Text of the message")
    private String message;
}
