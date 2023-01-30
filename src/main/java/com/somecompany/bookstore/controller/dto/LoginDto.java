package com.somecompany.bookstore.controller.dto;

import lombok.Data;
import lombok.ToString;


@Data
public class LoginDto {
    private Long id;
    private String username;
    @ToString.Exclude
    private String password;
}
