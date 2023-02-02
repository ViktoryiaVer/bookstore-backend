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
    @Pattern(regexp = RegExpConstant.NAME, message = "{msg.validation.first.name.not.valid}")
    @NotBlank(message = "{msg.validation.first.name.empty}")
    private String firstName;
    @Pattern(regexp = RegExpConstant.NAME, message = "{msg.validation.last.name.not.valid}")
    @NotBlank(message = "{msg.validation.last.name.empty}")
    private String lastName;
    @Email(message = "{msg.validation.email.not.valid}")
    @NotBlank(message = "{msg.validation.email.empty}")
    private String email;
    @Pattern(regexp = RegExpConstant.PHONE, message = "{msg.validation.phone.not.valid}")
    @Size(min = 11, message = "{msg.validation.phone.length}")
    @NotBlank(message = "{msg.validation.phone.empty}")
    private String phoneNumber;
    @NotNull(message = "{msg.validation.role.empty}")
    private Role role;
    @NotNull(message = "{msg.validation.login.null}")
    @Valid
    private LoginDto login;
}
