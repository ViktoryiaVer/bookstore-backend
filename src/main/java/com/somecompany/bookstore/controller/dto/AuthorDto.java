package com.somecompany.bookstore.controller.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AuthorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
}
