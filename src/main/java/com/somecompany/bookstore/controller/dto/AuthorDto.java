package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.constant.RegExpConstant;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class AuthorDto {
    private Long id;
    @Pattern(regexp = RegExpConstant.NAME, message = "{msg.validation.first.name.not.valid}")
    @NotBlank(message = "{msg.validation.first.name.empty}")
    private String firstName;
    @Pattern(regexp = RegExpConstant.NAME, message = "{msg.validation.last.name.not.valid}")
    @NotBlank(message = "{msg.validation.last.name.empty}")
    private String lastName;
    @NotNull(message = "{msg.validation.birthdate.empty}")
    private LocalDate birthdate;
}
