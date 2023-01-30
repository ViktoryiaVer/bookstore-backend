package com.somecompany.bookstore.mapper;

import com.somecompany.bookstore.model.entity.Author;
import com.somecompany.bookstore.controller.dto.AuthorDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDto toDto(Author author);

    Author toEntity(AuthorDto authorDto);
}
