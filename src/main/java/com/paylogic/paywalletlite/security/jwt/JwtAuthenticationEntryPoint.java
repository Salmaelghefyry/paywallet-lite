package com.paylogic.paywalletlite.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paylogic.paywalletlite.dto.response.ApiErrorResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiErrorResponseDto error = new ApiErrorResponseDto(
                "UNAUTHORIZED",
                "Authentication required: " + authException.getMessage(),
                null
        );

        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}