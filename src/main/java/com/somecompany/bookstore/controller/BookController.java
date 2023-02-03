package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.controller.dto.BookCreateDto;
import com.somecompany.bookstore.mapper.BookMapper;
import com.somecompany.bookstore.mapper.BookCreateMapper;
import com.somecompany.bookstore.controller.dto.BookDto;
import com.somecompany.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/books/")
public class BookController {
    private final BookService bookService;
    private final BookMapper mapper;
    private final BookCreateMapper writeMapper;

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks(Pageable pageable) {
        return ResponseEntity.ok(bookService.getAll(pageable).stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(bookService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookCreateDto book) {
        BookDto savedBook = mapper.toDto(bookService.save(writeMapper.toEntity(book)));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PutMapping
    public ResponseEntity<BookDto> updateBook(@Valid @RequestBody BookCreateDto book) {
        BookDto updatedBook = mapper.toDto(bookService.update(writeMapper.toEntity(book)));
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookDto> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).build();
    }
}
