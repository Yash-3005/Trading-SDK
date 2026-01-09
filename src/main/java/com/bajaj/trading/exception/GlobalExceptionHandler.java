package com.bajaj.trading.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler
 * Catches all exceptions thrown by controllers and returns proper error responses
 */
@RestControllerAdvice  // Applies to all @RestController classes
@Slf4j
public class GlobalExceptionHandler {
    
    /**
     * Handle IllegalArgumentException
     * Thrown for validation errors and business logic violations
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Validation error: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            ex.getMessage(),
            LocalDateTime.now()
        );
        
        return ResponseEntity.badRequest().body(error);
    }
    
    /**
     * Handle validation errors from @Valid annotation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Request validation failed");
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Failed",
            errors.toString(),
            LocalDateTime.now()
        );
        
        return ResponseEntity.badRequest().body(error);
    }
    
    /**
     * Handle all other unexpected exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "An unexpected error occurred. Please try again later.",
            LocalDateTime.now()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
    /**
     * Custom Error Response Structure
     */
    record ErrorResponse(
        int status,
        String error,
        String message,
        LocalDateTime timestamp
    ) {}
}

/**
 * INTERVIEW EXPLANATION:
 * 
 * Q: What is @RestControllerAdvice?
 * A: Global exception handler for all REST controllers
 *    Without this, each controller needs its own try-catch
 *    With this, all exceptions are caught in one place
 *    
 *    Benefits:
 *    - Centralized error handling
 *    - Consistent error format
 *    - Cleaner controller code
 * 
 * Q: What is @ExceptionHandler?
 * A: Marks method that handles specific exception type
 *    
 *    @ExceptionHandler(IllegalArgumentException.class)
 *    → Catches all IllegalArgumentException
 *    
 *    When controller throws exception:
 *    Controller → throws → Global Handler catches → Returns error response
 * 
 * Q: Why different handlers for different exceptions?
 * A: Different types = different HTTP status codes
 *    
 *    IllegalArgumentException → 400 Bad Request
 *    (User's fault: invalid input, insufficient holdings)
 *    
 *    NullPointerException → 500 Internal Server Error
 *    (Our fault: programming error)
 *    
 *    ResourceNotFoundException → 404 Not Found
 *    (Resource doesn't exist)
 * 
 * Q: What is the ErrorResponse record?
 * A: Standardized error response format
 *    
 *    Example JSON response:
 *    {
 *      "status": 400,
 *      "error": "Validation Error",
 *      "message": "Quantity must be greater than 0",
 *      "timestamp": "2026-01-09T03:30:00"
 *    }
 *    
 *    Clients always get consistent error format
 * 
 * Q: What's the difference between record and class?
 * A: Record (Java 14+) is immutable data holder
 *    
 *    Old way (class):
 *    class ErrorResponse {
 *        private int status;
 *        private String error;
 *        // Constructor, getters, equals, toString...
 *    }
 *    
 *    New way (record):
 *    record ErrorResponse(int status, String error) {}
 *    
 *    Much simpler! Auto-generates everything.
 * 
 * Q: Walk through error flow when invalid order is placed
 * A: 1. User sends: POST /api/v1/orders with quantity = -5
 *    2. OrderController receives request
 *    3. OrderService validates: quantity must be > 0
 *    4. Service throws IllegalArgumentException
 *    5. GlobalExceptionHandler catches it
 *    6. Handler creates ErrorResponse
 *    7. Returns 400 Bad Request with error JSON
 *    8. Client receives clear error message
 * 
 * Q: Why log.error() inside handlers?
 * A: For debugging and monitoring
 *    - Logs appear in server console/file
 *    - Can track error patterns
 *    - Helps debug production issues
 *    - Client doesn't see log, only ErrorResponse
 */