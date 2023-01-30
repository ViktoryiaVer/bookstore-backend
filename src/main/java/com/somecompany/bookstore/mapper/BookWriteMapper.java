package com.somecompany.bookstore.mapper;

import com.somecompany.bookstore.model.entity.Author;
import com.somecompany.bookstore.model.entity.Book;
import com.somecompany.bookstore.controller.dto.BookWriteDto;
import com.somecompany.bookstore.service.AuthorService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring", uses = AuthorMapper.class)
public abstract class BookWriteMapper {
    @Autowired
    private AuthorService authorService;

    @Mapping(source = "authorIds", target = "authors")
    public abstract Book toEntity(BookWriteDto book);

    protected Author getAuthorById(Long id) {
        return authorService.getById(id);
    }
}
