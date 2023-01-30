package com.somecompany.bookstore.mapper;

import com.somecompany.bookstore.controller.dto.OrderDetailWriteDto;
import com.somecompany.bookstore.model.entity.Book;
import com.somecompany.bookstore.model.entity.OrderDetail;
import com.somecompany.bookstore.service.BookService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {BookWriteMapper.class})
public abstract class OrderDetailWriteMapper {
    @Autowired
    private BookService bookService;

    @Mapping(target = "order", ignore = true)
    @Mapping(source = "bookId", target = "book")
    public abstract OrderDetail toEntity(OrderDetailWriteDto orderDetailDto);

    protected Book getBookById(Long id) {
        return bookService.getById(id);
    }
}
