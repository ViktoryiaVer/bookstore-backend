package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.constant.RegExpConstant;
import com.somecompany.bookstore.model.entity.enums.Role;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    private Long id;
    @NotBlank(message = "{msg.validation.first.name.empty}")
    @Pattern(regexp = RegExpConstant.NAME, message = "{msg.validation.first.name.not.valid}")
    private String firstName;
    @NotBlank(message = "{msg.validation.last.name.empty}")
    @Pattern(regexp = RegExpConstant.NAME, message = "{msg.validation.last.name.not.valid}")
    private String lastName;
    @NotBlank(message = "{msg.validation.email.empty}")
    @Email(message = "{msg.validation.email.not.valid}")
    private String email;
    @NotBlank(message = "{msg.validation.phone.empty}")
    @Size(min = 11, message = "{msg.validation.phone.length}")
    @Pattern(regexp = RegExpConstant.PHONE, message = "{msg.validation.phone.not.valid}")
    private String phoneNumber;
    @NotNull(message = "{msg.validation.role.empty}")
    private Role role;
    @Valid
    @NotNull(message = "{msg.validation.login.null}")
    private LoginDto login;
}
