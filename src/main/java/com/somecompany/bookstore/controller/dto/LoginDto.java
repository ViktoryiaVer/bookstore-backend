package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.constant.RegExpConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Schema(description = "dto for operations with user login data")
public class LoginDto {
    @Schema(description = "Id of the login")
    private Long id;

    @Schema(description = "Username of the user login")
    @NotBlank(message = "{msg.validation.username.empty}")
    @Pattern(regexp = RegExpConstant.USERNAME, message = "{msg.validation.username.not.valid}")
    private String username;

    @ToString.Exclude
    @Schema(description = "Password of the user login")
    @NotBlank(message = "{msg.validation.password.empty}")
    @Size(min = 8, message = "{msg.validation.password.length}")
    @Pattern(regexp = RegExpConstant.PASSWORD, message = "{msg.validation.password.not.valid}")
    private String password;
}
