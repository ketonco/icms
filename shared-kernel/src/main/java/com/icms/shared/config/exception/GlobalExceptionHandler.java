package com.icms.shared.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.icms.shared.dto.RestResponse;
import com.icms.shared.exceptions.BusinessRuleException;
import com.icms.shared.exceptions.EntityNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice 
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<RestResponse<Void>> handleNoResourceFound(NoResourceFoundException ex) {
        // TODO (pending P-07): codigo "000" hardcodeado y mensaje en espanol sin i18n; crear codigos de negocio y resolver via MessageResolver. Ver pending.md
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
            .body(RestResponse.error(
                HttpStatus.NOT_FOUND.value(),
                "URL no encontrada: " + ex.getResourcePath(),
                "000",
                null,
                HttpStatus.NOT_FOUND.getReasonPhrase()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<Void>> handleException(Exception ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String uri = request.getRequestURI();

        return ResponseEntity
                .status(status)
                .body(RestResponse.error(
                    status.value(),
                    ex.getMessage(),
                    // TODO (pending P-07): codigo "000" generico sin i18n en handler de Exception; definir codigo de negocio. Ver pending.md
                    "000", // Código de error para regla de negocio
                    uri,
                    status.getReasonPhrase() // Detailed error message like "Internal Server Error" or "Not Found"
                ));

    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<RestResponse<Void>> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        String uri = request.getRequestURI();

        return ResponseEntity
                .status(status)
                .body(RestResponse.error(
                    status.value(),
                    ex.getMessage(),
                    ex.getCode(), // Código de error para regla de negocio
                    uri,
                    status.getReasonPhrase() // Detailed error message like "Not Found"
                ));

    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<RestResponse<Void>> handleBusinessRuleException(BusinessRuleException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String uri = request.getRequestURI();

        return ResponseEntity
                .status(status)
                .body(RestResponse.error(
                    status.value(),
                    ex.getMessage(),
                    ex.getCode(), // Código de error para regla de negocio
                    uri,
                    status.getReasonPhrase() // Detailed error message like "Bad Request"
                ));

    }

}
