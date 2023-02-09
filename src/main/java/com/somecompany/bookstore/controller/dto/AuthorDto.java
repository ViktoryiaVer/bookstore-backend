package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.constant.RegExpConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Schema(description = "dto for operations with author")
public class AuthorDto {
    @Schema(description = "Id of the author")
    private Long id;

    @Schema(description = "First name of the author")
    @NotBlank(message = "{msg.validation.first.name.empty}")
    @Pattern(regexp = RegExpConstant.NAME, message = "{msg.validation.first.name.not.valid}")
    private String firstName;

    @Schema(description = "Last name of the author")
    @NotBlank(message = "{msg.validation.last.name.empty}")
    @Pattern(regexp = RegExpConstant.NAME, message = "{msg.validation.last.name.not.valid}")
    private String lastName;

    @NotNull(message = "{msg.validation.birthdate.empty}")
    @Schema(description = "Birthdate of the author")
    private LocalDate birthdate;
}
