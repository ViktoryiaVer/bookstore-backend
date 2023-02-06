package com.somecompany.bookstore;

import com.somecompany.bookstore.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .csrf()
                .disable()

                .cors()
                .and()

                .headers()
                .frameOptions()
                .sameOrigin()
                .and()
                .mvcMatcher("/api/auth/login/**").httpBasic().and()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.POST, "/api/auth/login/**", "/api/auth/signup/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/api/users/**", "/api/books/**", "/api/authors/**", "/api/orders/**", "/api/payments/**").authenticated()
                .mvcMatchers(HttpMethod.POST, "/api/users/**", "/api/books/**", "/api/authors/**", "/api/orders/**", "/api/payments/**").authenticated()
                .mvcMatchers(HttpMethod.PUT, "/api//users/**", "/api/books/**", "/api/authors/**", "/api/orders/**", "/api/payments/**").authenticated()
                .mvcMatchers(HttpMethod.DELETE, "/api/users/**", "/api/books/**", "/api/authors/**", "/api/orders/**", "/api/payments/**").authenticated()
                .anyRequest().denyAll()
                .and()

                .authenticationProvider(authProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
