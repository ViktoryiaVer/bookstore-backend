package com.somecompany.bookstore.controller.dto.error;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ValidationResultDto extends ErrorDto {
    private static final String DEFAULT_ERROR_MESSAGE = "Sent data violates validation constraints";
    private Map<String, List<String>> validationProblems;

    public ValidationResultDto() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public ValidationResultDto(Map<String, List<String>> validationProblems) {
        this();
        this.validationProblems = validationProblems;
    }
}
