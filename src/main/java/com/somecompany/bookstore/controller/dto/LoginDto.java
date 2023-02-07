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
    @NotBlank(message = "{msg.validation.username.empty}")
    @Pattern(regexp = RegExpConstant.USERNAME, message = "{msg.validation.username.not.valid}")
    private String username;
    @ToString.Exclude
    @NotBlank(message = "{msg.validation.password.empty}")
    @Size(min = 8, message = "{msg.validation.password.length}")
    @Pattern(regexp = RegExpConstant.PASSWORD, message = "{msg.validation.password.not.valid}")
    private String password;
}
