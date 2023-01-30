package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.mapper.UserMapper;
import com.somecompany.bookstore.service.UserService;
import com.somecompany.bookstore.controller.dto.UserDto;
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
@RequestMapping("api/users/")
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;
    @GetMapping()
    public List<UserDto> getAllUsers() {
        return userService.getAll().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return mapper.toDto(userService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserDto user) {
        return mapper.toDto(userService.save(mapper.toEntity(user)));
    }
    @PutMapping()
    public UserDto updateUser(@RequestBody UserDto user) {
        return mapper.toDto(userService.update(mapper.toEntity(user)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
