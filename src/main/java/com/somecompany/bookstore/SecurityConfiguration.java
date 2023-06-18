package com.somecompany.bookstore;

import com.somecompany.bookstore.constant.EndpointConstant;
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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
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

                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()

                .cors()
                .and()

                .headers()
                .frameOptions()
                .sameOrigin()
                .and()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.POST, EndpointConstant.LOGIN, EndpointConstant.SIGNUP).permitAll()
                .mvcMatchers(HttpMethod.GET, EndpointConstant.SWAGGER, EndpointConstant.API_DOCS).permitAll()
                .mvcMatchers(HttpMethod.GET, EndpointConstant.USERS, EndpointConstant.BOOKS, EndpointConstant.AUTHORS, EndpointConstant.ORDERS, EndpointConstant.PAYMENTS).authenticated()
                .mvcMatchers(HttpMethod.POST, EndpointConstant.USERS, EndpointConstant.BOOKS, EndpointConstant.AUTHORS, EndpointConstant.ORDERS, EndpointConstant.PAYMENTS).authenticated()
                .mvcMatchers(HttpMethod.PUT, EndpointConstant.USERS, EndpointConstant.BOOKS, EndpointConstant.AUTHORS, EndpointConstant.ORDERS, EndpointConstant.PAYMENTS).authenticated()
                .mvcMatchers(HttpMethod.DELETE, EndpointConstant.USERS, EndpointConstant.BOOKS, EndpointConstant.AUTHORS, EndpointConstant.ORDERS, EndpointConstant.PAYMENTS).authenticated()
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
                .mvcMatcher(EndpointConstant.LOGIN).authorizeRequests().anyRequest().authenticated()
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
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
