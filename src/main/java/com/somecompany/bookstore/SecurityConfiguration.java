package com.somecompany.bookstore;

import com.somecompany.bookstore.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final BasicAuthenticationEntryPoint entryPoint;

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
                .authorizeRequests()
                .mvcMatchers(HttpMethod.POST, "/api/auth/login/**", "/api/auth/signup/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs").permitAll()
                .mvcMatchers(HttpMethod.GET, "/api/users/**", "/api/books/**", "/api/authors/**", "/api/orders/**", "/api/payments/**").authenticated()
                .mvcMatchers(HttpMethod.POST, "/api/users/**", "/api/books/**", "/api/authors/**", "/api/orders/**", "/api/payments/**").authenticated()
                .mvcMatchers(HttpMethod.PUT, "/api/users/**", "/api/books/**", "/api/authors/**", "/api/orders/**", "/api/payments/**").authenticated()
                .mvcMatchers(HttpMethod.DELETE, "/api/users/**", "/api/books/**", "/api/authors/**", "/api/orders/**", "/api/payments/**").authenticated()
                .anyRequest().denyAll()
                .and()

                .authenticationProvider(authProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain basicAuthSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .mvcMatcher("/api/auth/login/**").authorizeRequests().anyRequest().authenticated()
                .and()
                .csrf().disable()
                .cors()
                .and()
                .httpBasic().authenticationEntryPoint(entryPoint)
                .and()
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

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
