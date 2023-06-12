package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.controller.dto.BookCreateDto;
import com.somecompany.bookstore.controller.dto.response.BooksWithPaginationDto;
import com.somecompany.bookstore.controller.dto.response.MessageDto;
import com.somecompany.bookstore.controller.dto.response.ValidationResultDto;
import com.somecompany.bookstore.mapper.BookMapper;
import com.somecompany.bookstore.mapper.BookCreateMapper;
import com.somecompany.bookstore.controller.dto.BookDto;
import com.somecompany.bookstore.model.entity.Book;
import com.somecompany.bookstore.service.api.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/books")
@Tag(name = "books", description = "operations with books")
public class BookController {
    private final BookService bookService;
    private final BookMapper mapper;
    private final BookCreateMapper writeMapper;

    @GetMapping
    @Operation(summary = "Get all books (paginated result)")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the books",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BooksWithPaginationDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid pageable object supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<BooksWithPaginationDto> getAllBooks(@ParameterObject Pageable pageable) {
        Page<Book> bookPage = bookService.getAll(pageable);
        BooksWithPaginationDto booksWithPaginationDto = new BooksWithPaginationDto();
        booksWithPaginationDto.setBooks(bookPage.getContent().stream().map(mapper::toDto).toList());
        booksWithPaginationDto.setTotalPages(bookPage.getTotalPages());
        return ResponseEntity.ok(booksWithPaginationDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by its id")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<BookDto> getBook(@Parameter(description = "Id of the book to be found",
            required = true) @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(bookService.getById(id)));
    }

    @PostMapping
    @Operation(summary = "Create a book")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book is created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class))}),
            @ApiResponse(responseCode = "400", description = "Some of the book properties are not valid",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationResultDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied: user has no authority for creating a book",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Server error by creating a book",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookCreateDto book) {
        BookDto savedBook = mapper.toDto(bookService.save(writeMapper.toEntity(book)));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PutMapping
    @Operation(summary = "Update a book")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book is updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class))}),
            @ApiResponse(responseCode = "400", description = "Some of the book properties are not valid",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationResultDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied: user has no authority for updating a book",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Server error by updating a book",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<BookDto> updateBook(@Valid @RequestBody BookCreateDto book) {
        BookDto updatedBook = mapper.toDto(bookService.update(writeMapper.toEntity(book)));
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "205", description = "Book is deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied: user has no authority for deleting a book",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<BookDto> deleteBook(@Parameter(description = "Id of the book to be deleted",
            required = true) @PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).build();
    }
}
