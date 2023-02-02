package com.somecompany.bookstore.mapper;

import com.somecompany.bookstore.model.entity.Book;
import com.somecompany.bookstore.controller.dto.BookDto;
import org.mapstruct.Mapper;

@Mapper(uses = AuthorMapper.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toEntity(BookDto bookDto);
}
