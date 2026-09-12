package com.icms.shared.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icms.shared.dto.RestResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component 
public class CustomSecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public CustomSecurityExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Maneja peticiones No Autenticadas (401)
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        writeRestResponse(request, response, HttpStatus.UNAUTHORIZED, "No autenticado: Debe proporcionar un token válido.");
    }

    // Maneja peticiones Sin Permisos Suficientes (403)
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        writeRestResponse(request, response, HttpStatus.FORBIDDEN, "Acceso denegado: No posee los permisos requeridos.");
    }

    private void writeRestResponse(HttpServletRequest request, HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        RestResponse<Void> errorResponse = RestResponse.error(
                status.value(),
                "ERROR",
                message,
                request.getRequestURI(),
                ""
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
