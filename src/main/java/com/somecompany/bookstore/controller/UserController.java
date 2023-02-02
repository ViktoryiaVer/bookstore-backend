package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.exception.ValidationException;
import com.somecompany.bookstore.mapper.UserMapper;
import com.somecompany.bookstore.service.UserService;
import com.somecompany.bookstore.controller.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("api/users/")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    @GetMapping
    public List<UserDto> getAllUsers(Pageable pageable) {
        return userService.getAll(pageable).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return mapper.toDto(userService.getById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto user, Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }

        UserDto savedUser = mapper.toDto(userService.save(mapper.toEntity(user)));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto user, Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }

        UserDto updatedUser = mapper.toDto(userService.update(mapper.toEntity(user)));
        return ResponseEntity.ok(updatedUser);
    }

    @ResponseStatus(HttpStatus.RESET_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
