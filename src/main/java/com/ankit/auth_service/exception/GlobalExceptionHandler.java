package com.ankit.auth_service.exception;

import com.ankit.auth_service.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles @Valid failures in incoming DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse response = ErrorResponse.builder()
                .message("Validation failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now())
                .validationErrors(errors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handles incorrect login details
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .message("Invalid email or password")
                .status(HttpStatus.UNAUTHORIZED.value())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Handles custom thrown exceptions like "Email already exists"
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}