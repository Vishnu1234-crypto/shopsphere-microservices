package com.shopsphere.cart_service.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            ResourceNotFoundException ex) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                ex.getMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(
            IllegalArgumentException ex) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        Map<String, Object> body = new LinkedHashMap<>();

        body.put("timestamp", LocalDateTime.now());
        body.put("status", 400);
        body.put("error", "Validation Failed");
        body.put("message", errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    /*
     * Product/Inventory/Order/Payment service returned 404.
     *
     * Feign converts the remote HTTP 404 into
     * FeignException.NotFound.
     */
    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<Map<String, Object>> handleFeignNotFound(
            FeignException.NotFound ex) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                "Required resource was not found in another service"
        );
    }

    /*
     * Other Feign errors are genuine service communication
     * problems, so keep them as 502.
     */
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeignException(
            FeignException ex) {

        return buildResponse(
                HttpStatus.BAD_GATEWAY,
                "Service Communication Error",
                "Unable to communicate with a required service"
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(
            Exception ex) {

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred"
        );
    }

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String error,
            String message) {

        Map<String, Object> body = new LinkedHashMap<>();

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