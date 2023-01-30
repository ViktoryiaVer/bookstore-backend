package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.mapper.AuthorMapper;
import com.somecompany.bookstore.controller.dto.AuthorDto;
import com.somecompany.bookstore.service.AuthorService;
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
@RequestMapping("api/authors/")
public class AuthorController {
    private final AuthorService authorService;
    private final AuthorMapper mapper;
    @GetMapping()
    public List<AuthorDto> getAllAuthors() {
        return authorService.getAll().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public AuthorDto getAuthor(@PathVariable Long id) {
        return mapper.toDto(authorService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto createAuthor(@RequestBody AuthorDto author) {
        return mapper.toDto(authorService.save(mapper.toEntity(author)));
    }

    @PutMapping()
    public AuthorDto updateAuthor(@RequestBody AuthorDto author) {
        return mapper.toDto(authorService.update(mapper.toEntity(author)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteById(id);
    }
}
