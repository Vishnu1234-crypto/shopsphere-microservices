package com.shopsphere.inventory_service.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>>
    handleNotFound(
            ResourceNotFoundException ex) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                ex.getMessage()
        );
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Map<String, Object>>
    handleInsufficientStock(
            InsufficientStockException ex) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Insufficient Stock",
                ex.getMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>>
    handleBadRequest(
            IllegalArgumentException ex) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>>
    handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors =
                new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        Map<String, Object> body =
                new LinkedHashMap<>();

        body.put(
                "timestamp",
                LocalDateTime.now()
        );

        body.put(
                "status",
                HttpStatus.BAD_REQUEST.value()
        );

        body.put(
                "error",
                "Validation Failed"
        );

        body.put(
                "message",
                errors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>>
    handleGeneral(Exception ex) {

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred"
        );
    }

    private ResponseEntity<Map<String, Object>>
    buildResponse(
            HttpStatus status,
            String error,
            String message) {

        Map<String, Object> body =
                new LinkedHashMap<>();

        body.put(
                "timestamp",
                LocalDateTime.now()
        );

        body.put(
                "status",
                status.value()
        );

        body.put(
                "error",
                error
        );

        body.put(
                "message",
                message
        );

        return ResponseEntity
                .status(status)
                .body(body);
    }
}