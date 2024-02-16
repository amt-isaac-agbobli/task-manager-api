package com.isaac.taskmanagementapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * This method handles HttpException.
     * It returns a ResponseEntity with the exception message and HTTP status.
     *
     * @param httpException the exception to handle
     * @return a ResponseEntity with the exception message and HTTP status
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(value= {HttpException.class})
    public ResponseEntity<Object> httpException
    (HttpException httpException)
    {
        ExceptionHandler exceptionHandler = new ExceptionHandler(
                httpException.getMessage(),
                httpException.getCause(),
                httpException.getHttpStatus()
        );

        return new ResponseEntity<>(exceptionHandler, httpException.getHttpStatus());
    }

    /**
     * This method handles MethodArgumentNotValidException.
     * It returns a ResponseEntity with the validation errors and HTTP status.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with the validation errors and HTTP status
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
