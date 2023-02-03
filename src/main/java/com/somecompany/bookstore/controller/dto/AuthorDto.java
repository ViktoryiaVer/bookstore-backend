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
    @NotBlank(message = "{msg.validation.first.name.empty}")
    @Pattern(regexp = RegExpConstant.NAME, message = "{msg.validation.first.name.not.valid}")
    private String firstName;
    @NotBlank(message = "{msg.validation.last.name.empty}")
    @Pattern(regexp = RegExpConstant.NAME, message = "{msg.validation.last.name.not.valid}")
    private String lastName;
    @NotNull(message = "{msg.validation.birthdate.empty}")
    private LocalDate birthdate;
}
