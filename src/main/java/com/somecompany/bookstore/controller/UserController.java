package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.controller.dto.response.MessageDto;
import com.somecompany.bookstore.controller.dto.response.ValidationResultDto;
import com.somecompany.bookstore.mapper.UserMapper;
import com.somecompany.bookstore.service.UserService;
import com.somecompany.bookstore.controller.dto.UserDto;
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
@RequestMapping("api/users/")
@Tag(name = "users", description = "operations with users")
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(summary = "Get all users (paginated result)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the users",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(
                            schema = @Schema(implementation = UserDto.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid pageable object supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<List<UserDto>> getAllUsers(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(userService.getAll(pageable).stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by its id")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public UserDto getUser(@Parameter(description = "Id of the user to be found",
            required = true) @PathVariable Long id) {
        return mapper.toDto(userService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create a user")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User is created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "400", description = "Some of the user properties are not valid",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationResultDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied: user has no authority for creating a user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Server error by creating a user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto user) {
        UserDto savedUser = mapper.toDto(userService.save(mapper.toEntity(user)));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping
    @Operation(summary = "Update a user")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "400", description = "Some of the user properties are not valid",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationResultDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied: user has no authority for updating a user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Server error by updating a user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto user) {
        UserDto updatedUser = mapper.toDto(userService.update(mapper.toEntity(user)));
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "205", description = "User is deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied: user has no authority for deleting a user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<UserDto> deleteUser(@Parameter(description = "Id of the book to be deleted",
            required = true) @PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).build();
    }
}
