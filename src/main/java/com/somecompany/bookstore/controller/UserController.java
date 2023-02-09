package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.mapper.UserMapper;
import com.somecompany.bookstore.service.UserService;
import com.somecompany.bookstore.controller.dto.UserDto;
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
@RequestMapping("api/users/")
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<List<UserDto>> getAllUsers(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(userService.getAll(pageable).stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public UserDto getUser(@PathVariable Long id) {
        return mapper.toDto(userService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto user) {
        UserDto savedUser = mapper.toDto(userService.save(mapper.toEntity(user)));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto user) {
        UserDto updatedUser = mapper.toDto(userService.update(mapper.toEntity(user)));
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).build();
    }
}
