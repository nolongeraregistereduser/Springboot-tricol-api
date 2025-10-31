package com.tricol.springboottricolapi.exception;


import com.tricol.springboottricolapi.dto.ErrorResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;


@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {


        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDate.now())
                .status(404)
                .error(String.valueOf(HttpStatus.NOT_FOUND))
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DuplicateRessourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
            DuplicateRessourceException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDate.now())
                .status(409)
                .error(String.valueOf(HttpStatus.CONFLICT))
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }


    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStockException(
            InsufficientStockException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDate.now())
                .status(400)
                .error(String.valueOf(HttpStatus.BAD_REQUEST))
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(InvalidFifoOperationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFifoOperationException(
            InvalidFifoOperationException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDate.now())
                .status(400)
                .error(String.valueOf(HttpStatus.BAD_REQUEST))
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        val errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDate.now())
                .status(400)
                .error(String.valueOf(HttpStatus.BAD_REQUEST))
                .message("Validation failed: " + String.join(", ", errors))
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


}
