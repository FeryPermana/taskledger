package com.ferry.taskledger.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferry.taskledger.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.IOException;

@Component
public class SecurityExceptionHandler
        implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public SecurityExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String message = "Authentication required";

        if (authException instanceof BadCredentialsException) {
            message = "Invalid email or password";
        }

        ApiResponse<Void> apiResponse = new ApiResponse<>(
                401,
                message,
                null
        );

        response.getWriter().write(
                objectMapper.writeValueAsString(apiResponse)
        );
    }

   @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        String message = accessDeniedException.getMessage();

        if (message == null
                || message.isBlank()
                || message.equals("Access Denied")) {

        message = "You do not have permission to access this resource";
        }

        ApiResponse<Void> apiResponse = new ApiResponse<>(
                403,
                message,
                null
        );

        response.getWriter().write(
                objectMapper.writeValueAsString(apiResponse)
        );
    }
}