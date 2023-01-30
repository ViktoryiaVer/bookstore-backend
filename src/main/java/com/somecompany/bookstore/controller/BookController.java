package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.mapper.BookMapper;
import com.somecompany.bookstore.mapper.BookWriteMapper;
import com.somecompany.bookstore.controller.dto.BookDto;
import com.somecompany.bookstore.controller.dto.BookWriteDto;
import com.somecompany.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/books/")
public class BookController {
    private final BookService bookService;
    private final BookMapper mapper;
    private final BookWriteMapper writeMapper;

    @GetMapping()
    public List<BookDto> getAllBooks() {
        return bookService.getAll().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public BookDto getBook(@PathVariable Long id) {
        return mapper.toDto(bookService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@RequestBody BookWriteDto book) {
        return mapper.toDto(bookService.save(writeMapper.toEntity(book)));
    }

    @PutMapping()
    public BookDto updateBook(@RequestBody BookWriteDto book) {
        return mapper.toDto(bookService.update(writeMapper.toEntity(book)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
    }
}
