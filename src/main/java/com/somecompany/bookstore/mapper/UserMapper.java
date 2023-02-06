package com.somecompany.bookstore.mapper;

import com.somecompany.bookstore.controller.dto.LoginDto;
import com.somecompany.bookstore.model.entity.Login;
import com.somecompany.bookstore.model.entity.User;
import com.somecompany.bookstore.controller.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mapping(target = "login.password", ignore = true)
    public abstract UserDto toDto(User user);

    public abstract User toEntity(UserDto userDto);

    protected Login loginDtoToLoginWithPasswordEncoding(LoginDto loginDto) {
        Login login = new Login();
        login.setPassword(passwordEncoder.encode(loginDto.getPassword()));
        login.setUsername(loginDto.getUsername());
        return login;
    }
}
