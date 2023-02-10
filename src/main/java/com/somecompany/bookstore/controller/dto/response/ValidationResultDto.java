package com.somecompany.bookstore.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "dto for returning validation errors with their description")
public class ValidationResultDto extends MessageDto {
    private static final String DEFAULT_ERROR_MESSAGE = "Sent data violates validation constraints";
    @Schema(description = "Validation problem descriptions")
    private Map<String, List<String>> validationProblems;

    public ValidationResultDto() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public ValidationResultDto(Map<String, List<String>> validationProblems) {
        this();
        this.validationProblems = validationProblems;
    }
}
