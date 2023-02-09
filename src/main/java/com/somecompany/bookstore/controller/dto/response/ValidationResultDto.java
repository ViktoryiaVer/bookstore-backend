package com.somecompany.bookstore.controller.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValidationResultDto extends MessageDto {
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
