package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.mapper.AuthorMapper;
import com.somecompany.bookstore.controller.dto.AuthorDto;
import com.somecompany.bookstore.service.AuthorService;
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
public class AuthorController {
    private final AuthorService authorService;
    private final AuthorMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<List<AuthorDto>> getAllAuthors(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(authorService.getAll(pageable).stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(authorService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AuthorDto> createAuthor(@Valid @RequestBody AuthorDto author) {
        AuthorDto savedAuthor = mapper.toDto(authorService.save(mapper.toEntity(author)));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AuthorDto> updateAuthor(@Valid @RequestBody AuthorDto author) {
        AuthorDto updatedAuthor = mapper.toDto(authorService.update(mapper.toEntity(author)));
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AuthorDto> deleteAuthor(@PathVariable Long id) {
        authorService.deleteById(id);
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).build();
    }
}
