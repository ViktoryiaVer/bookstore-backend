package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.controller.dto.response.MessageDto;
import com.somecompany.bookstore.controller.dto.response.TokenDto;
import com.somecompany.bookstore.controller.dto.UserDto;
import com.somecompany.bookstore.controller.dto.response.ValidationResultDto;
import com.somecompany.bookstore.mapper.UserMapper;
import com.somecompany.bookstore.security.jwt.JwtUtils;
import com.somecompany.bookstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth/")
@Tag(name = "authentication", description = "login and sign up operations")
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "basicAuth",
        scheme = "basic")
public class AuthenticationController {
    private final JwtUtils jwtUtils;
    private final UserMapper mapper;
    private final UserService userService;
    private final MessageSource messageSource;

    @PostMapping("/login")
    @SecurityRequirement(name = "basicAuth")
    @Operation(summary = "Log in the system using basic authentication and get token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login was successful and token was generated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<?> authenticateUser(Authentication authentication) {
        if (authentication != null) {
            String jwt = jwtUtils.generateToken((UserDetails) authentication.getPrincipal());
            return ResponseEntity.ok(new TokenDto(jwt));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageDto(messageSource.getMessage("msg.error.login", null,
                    LocaleContextHolder.getLocale())));
        }
    }

    @SecurityRequirements
    @PostMapping("/signup")
    @Operation(summary = "Sign up")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registration was successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "400", description = "Some of the user properties are not valid",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationResultDto.class))}),
            @ApiResponse(responseCode = "500", description = "Server error by creating a user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<MessageDto> registerUser(@Valid @RequestBody UserDto user) {
        mapper.toDto(userService.save(mapper.toEntity(user)));
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDto(messageSource.getMessage("msg.registration.success", null,
                LocaleContextHolder.getLocale())));
    }
}
