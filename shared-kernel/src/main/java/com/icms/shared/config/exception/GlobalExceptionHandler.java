package com.icms.shared.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.icms.shared.Utils.MessageResolver;
import com.icms.shared.dto.RestResponse;
import com.icms.shared.exceptions.BusinessRuleException;
import com.icms.shared.exceptions.EntityNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice 
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public RestResponse<String> handleException(Exception ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String uri = request.getRequestURI();

        return RestResponse.error(
            status.value(),
            ex.getMessage(),
            "000", // Código de error para regla de negocio
            uri,
            status.getReasonPhrase() // Detailed error message like "Internal Server Error" or "Not Found"
        ); 

    }

    @ExceptionHandler(EntityNotFoundException.class)
    public RestResponse<String> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        String uri = request.getRequestURI();

        return RestResponse.error(
            status.value(),
            ex.getMessage(),
            ex.getCode(), // Código de error para regla de negocio
            uri,
            status.getReasonPhrase() // Detailed error message like "Not Found"
        ); 

    }

    @ExceptionHandler(BusinessRuleException.class)
    public RestResponse<String> handleBusinessRuleException(BusinessRuleException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String uri = request.getRequestURI();

        return RestResponse.error(
            status.value(),
            ex.getMessage(),
            ex.getCode(), // Código de error para regla de negocio
            uri,
            status.getReasonPhrase() // Detailed error message like "Bad Request"
        ); 

    }

}
