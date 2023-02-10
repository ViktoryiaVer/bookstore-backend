package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.constant.RegExpConstant;
import com.somecompany.bookstore.model.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Schema(description = "dto for operations with user")
public class UserDto {
    @Schema(description = "Id of the user")
    private Long id;

    @Schema(description = "First name of the user")
    @NotBlank(message = "{msg.validation.first.name.empty}")
    @Pattern(regexp = RegExpConstant.NAME, message = "{msg.validation.first.name.not.valid}")
    private String firstName;

    @Schema(description = "Last name of the user")
    @NotBlank(message = "{msg.validation.last.name.empty}")
    @Pattern(regexp = RegExpConstant.NAME, message = "{msg.validation.last.name.not.valid}")
    private String lastName;

    @Schema(description = "Email of the user")
    @NotBlank(message = "{msg.validation.email.empty}")
    @Email(message = "{msg.validation.email.not.valid}")
    private String email;

    @Schema(description = "Phone number of the user")
    @NotBlank(message = "{msg.validation.phone.empty}")
    @Size(min = 11, message = "{msg.validation.phone.length}")
    @Pattern(regexp = RegExpConstant.PHONE, message = "{msg.validation.phone.not.valid}")
    private String phoneNumber;

    @Schema(description = "Role of the user")
    @NotNull(message = "{msg.validation.role.empty}")
    private Role role;
    @Valid
    @NotNull(message = "{msg.validation.login.null}")
    private LoginDto login;
}
