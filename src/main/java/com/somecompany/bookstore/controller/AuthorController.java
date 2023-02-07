package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.mapper.AuthorMapper;
import com.somecompany.bookstore.controller.dto.AuthorDto;
import com.somecompany.bookstore.service.AuthorService;
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
@RequestMapping("api/authors/")
public class AuthorController {
    private final AuthorService authorService;
    private final AuthorMapper mapper;

    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAllAuthors(Pageable pageable) {
        return ResponseEntity.ok(authorService.getAll(pageable).stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(authorService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(@Valid @RequestBody AuthorDto author) {
        AuthorDto savedAuthor = mapper.toDto(authorService.save(mapper.toEntity(author)));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
    }

    @PutMapping
    public ResponseEntity<AuthorDto> updateAuthor(@Valid @RequestBody AuthorDto author) {
        AuthorDto updatedAuthor = mapper.toDto(authorService.update(mapper.toEntity(author)));
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuthorDto> deleteAuthor(@PathVariable Long id) {
        authorService.deleteById(id);
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).build();
    }
}