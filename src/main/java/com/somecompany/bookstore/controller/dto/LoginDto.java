package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.constant.RegExpConstant;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class LoginDto {
    private Long id;
    @Pattern(regexp = RegExpConstant.USERNAME, message = "{msg.validation.username.not.valid}")
    @NotBlank(message = "{msg.validation.username.empty}")
    private String username;
    @Pattern(regexp = RegExpConstant.PASSWORD, message = "{msg.validation.password.not.valid}")
    @Size(min = 8, message = "{msg.validation.password.length}")
    @NotBlank(message = "{msg.validation.password.empty}")
    @ToString.Exclude
    private String password;
}
