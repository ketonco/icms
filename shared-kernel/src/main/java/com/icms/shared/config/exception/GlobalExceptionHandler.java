package com.icms.shared.config.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.icms.shared.Utils.MessageResolver;
import com.icms.shared.dto.RestResponse;
import com.icms.shared.exceptions.BusinessRuleException;
import com.icms.shared.exceptions.EntityNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice 
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<RestResponse<Void>> handleNoResourceFound(NoResourceFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
            .body(RestResponse.error(
                HttpStatus.NOT_FOUND.value(),
                MessageResolver.resolveMessage("Res-001"),
                "Res-001",
                null,
                HttpStatus.NOT_FOUND.getReasonPhrase()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<Void>> handleException(Exception ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;// HTTP code for internal server error
        String uri = request.getRequestURI();

        return ResponseEntity
                .status(status)
                .body(RestResponse.error(
                    status.value(),
                    ex.getMessage(),
                    "E-001", // Business rule error code
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

    @ExceptionHandler({
        BusinessRuleException.class, 
        MethodArgumentNotValidException.class,
        ConstraintViolationException.class,
        HttpMessageNotReadableException.class,
        MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<RestResponse<Void>> handleBadRequestException(Exception ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String uri = request.getRequestURI();
        String code = ex instanceof BusinessRuleException ? ((BusinessRuleException) ex).getCode() : "E-002";
        Map<String, String> errors = ex instanceof MethodArgumentNotValidException ? handleValidationErrors((MethodArgumentNotValidException) ex) : null;
        String errorMessage = ex instanceof BusinessRuleException ? ex.getMessage() : MessageResolver.resolveMessage("E-002");

        return ResponseEntity
                .status(status)
                .body(RestResponse.error(
                    status.value(),
                    errorMessage,
                    code, // Código de error para regla de negocio
                    uri,
                    status.getReasonPhrase(), // Detailed error message like "Bad Request"
                    errors
                ));
    }

    @SuppressWarnings ("null")
    Map<String, String> handleValidationErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                    FieldError::getField, 
                    FieldError::getDefaultMessage,
                    (existing, replacement) -> existing + ", " + replacement, // In case of duplicate keys, concatenate the error messages
                    LinkedHashMap::new // Use LinkedHashMap to preserve the order of the field errors
                ));
    }

}
