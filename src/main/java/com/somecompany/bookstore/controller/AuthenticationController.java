package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.controller.dto.response.MessageDto;
import com.somecompany.bookstore.controller.dto.response.TokenDto;
import com.somecompany.bookstore.controller.dto.UserDto;
import com.somecompany.bookstore.mapper.UserMapper;
import com.somecompany.bookstore.security.jwt.JwtUtils;
import com.somecompany.bookstore.service.UserService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
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
    public ResponseEntity<?> authenticateUser(Authentication authentication) {
        if (authentication != null) {
            String jwt = jwtUtils.generateToken((UserDetails) authentication.getPrincipal());
            return ResponseEntity.ok(new TokenDto(jwt));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageDto(messageSource.getMessage("msg.login.error", null,
                    LocaleContextHolder.getLocale())));
        }

    }
    @SecurityRequirements
    @PostMapping("/signup")
    public ResponseEntity<MessageDto> registerUser(@Valid @RequestBody UserDto user) {
        mapper.toDto(userService.save(mapper.toEntity(user)));
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDto(messageSource.getMessage("msg.registration.success", null,
                LocaleContextHolder.getLocale())));
    }
}
