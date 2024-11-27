package com.app.sales.security;

import com.app.sales.exception.InvalidApiKeyException;
import com.app.sales.exception.MissingApiKeyException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        int status = HttpServletResponse.SC_UNAUTHORIZED;
        String error = "Unauthorized";
        String message = authException.getMessage();

        if (authException instanceof MissingApiKeyException) {
            status = HttpServletResponse.SC_BAD_REQUEST;
            error = "Bad Request";
        } else if (authException instanceof InvalidApiKeyException) {
            status = HttpServletResponse.SC_UNAUTHORIZED;
            error = "Unauthorized";
        }

        response.setStatus(status);
        response.setContentType("application/json");

        String json = String.format(
                "{\"error\": \"%s\", \"message\": \"%s\"}",
                error,
                message
        );

        response.getWriter().write(json);
    }
}