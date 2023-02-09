package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.controller.dto.response.MessageDto;
import com.somecompany.bookstore.controller.dto.response.ValidationResultDto;
import com.somecompany.bookstore.mapper.AuthorMapper;
import com.somecompany.bookstore.controller.dto.AuthorDto;
import com.somecompany.bookstore.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/authors/")
@Tag(name = "authors", description = "operations with authors")
public class AuthorController {
    private final AuthorService authorService;
    private final AuthorMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(summary = "Get all authors (paginated result)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the authors",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(
                            schema = @Schema(implementation = AuthorDto.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid pageable object supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<List<AuthorDto>> getAllAuthors(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(authorService.getAll(pageable).stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an author by its id")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the author",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<AuthorDto> getAuthor(@Parameter(description = "Id of the author to be found",
            required = true) @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(authorService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create an author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author is created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class))}),
            @ApiResponse(responseCode = "400", description = "Some of the author properties are not valid",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationResultDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied: user has no authority for creating an author",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Server error by creating an author",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<AuthorDto> createAuthor(@Valid @RequestBody AuthorDto author) {
        AuthorDto savedAuthor = mapper.toDto(authorService.save(mapper.toEntity(author)));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update an author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author is updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class))}),
            @ApiResponse(responseCode = "400", description = "Some of the author properties are not valid",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationResultDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied: user has no authority for updating an author",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Server error by updating an author",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<AuthorDto> updateAuthor(@Valid @RequestBody AuthorDto author) {
        AuthorDto updatedAuthor = mapper.toDto(authorService.update(mapper.toEntity(author)));
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete an author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "205", description = "Author is deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied: user has no authority for deleting an author",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<AuthorDto> deleteAuthor(@Parameter(description = "Id of the author to be deleted",
            required = true) @PathVariable Long id) {
        authorService.deleteById(id);
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).build();
    }
}
