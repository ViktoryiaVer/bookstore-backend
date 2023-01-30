package com.somecompany.bookstore.controller.dto;

import com.somecompany.bookstore.controller.dto.enums.RoleDto;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private RoleDto role;
    private LoginDto login;
}
