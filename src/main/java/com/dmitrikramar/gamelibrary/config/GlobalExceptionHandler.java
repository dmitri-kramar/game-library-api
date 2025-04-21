package com.dmitrikramar.gamelibrary.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

/**
 * Global exception handler for the application.
 * <p>
 * This class provides centralized handling of common exceptions thrown across the application.
 * It converts internal exceptions into appropriate HTTP responses with meaningful error messages.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles cases when an entity is not found in the database.
     *
     * @param ex the thrown NoSuchElementException
     * @return 404 NOT FOUND with the exception message
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNotFoundException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles illegal argument exceptions, typically thrown when user input is invalid.
     *
     * @param ex the thrown IllegalArgumentException
     * @return 400 BAD REQUEST with the exception message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    /**
     * Handles illegal state exceptions, typically related to unexpected or inconsistent application state.
     *
     * @param ex the thrown IllegalStateException
     * @return 400 BAD REQUEST with the exception message
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    /**
     * Handles authorization errors when access to a resource is denied.
     *
     * @param ex the thrown AuthorizationDeniedException
     * @return 403 FORBIDDEN with the exception message
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    /**
     * Handles validation errors for request bodies annotated with @Valid.
     *
     * @param ex the thrown MethodArgumentNotValidException
     * @return 400 BAD REQUEST with a list of validation error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList());
    }

    /**
     * Handles cases where the request body cannot be parsed (e.g. malformed JSON).
     *
     * @param ex the thrown HttpMessageNotReadableException
     * @return 400 BAD REQUEST with the exception message
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    /**
     * Handles login failures due to incorrect username or password.
     *
     * @return 401 UNAUTHORIZED with a static message
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }
}
