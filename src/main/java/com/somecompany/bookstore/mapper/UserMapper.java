package com.somecompany.bookstore.mapper;

import com.somecompany.bookstore.model.entity.User;
import com.somecompany.bookstore.controller.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "login.password", ignore = true)
    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
