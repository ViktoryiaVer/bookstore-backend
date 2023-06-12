package com.somecompany.bookstore.mapper;

import com.somecompany.bookstore.model.entity.Author;
import com.somecompany.bookstore.model.entity.Book;
import com.somecompany.bookstore.controller.dto.BookCreateDto;
import com.somecompany.bookstore.service.api.AuthorService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(uses = AuthorMapper.class)
public abstract class BookCreateMapper {
    @Autowired
    private AuthorService authorService;

    @Mapping(source = "authorIds", target = "authors")
    public abstract Book toEntity(BookCreateDto book);

    protected Author getAuthorById(Long id) {
        return authorService.getById(id);
    }
}
