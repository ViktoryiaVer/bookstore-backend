package com.somecompany.bookstore.security;

import com.google.gson.Gson;
import com.somecompany.bookstore.controller.dto.response.MessageDto;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
            throws IOException {
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().print(new Gson().toJson(new MessageDto(authEx.getMessage())));
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("Bookstore");
        super.afterPropertiesSet();
    }
}
