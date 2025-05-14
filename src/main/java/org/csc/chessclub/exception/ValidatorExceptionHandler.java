package org.csc.chessclub.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

@ControllerAdvice
@Order(1)
public class ValidatorExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidErrorMessage> validationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> violations = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((violation) -> {
            String fieldName = ((FieldError) violation).getField();
            String errorMessage = violation.getDefaultMessage();
            violations.put(fieldName, errorMessage);
        });
        ValidErrorMessage errorMessage = new ValidErrorMessage(HttpStatus.BAD_REQUEST.value(), violations);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidErrorMessage> paramValidationException(ConstraintViolationException ex) {
        Map<String, String> violations = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String message = violation.getMessage();
            String field = StreamSupport
                    .stream(violation.getPropertyPath().spliterator(), false)
                    .reduce((first, second) -> second)
                    .map(Path.Node::getName)
                    .orElse("unknown");

            violations.put(field, message);
        });
        ValidErrorMessage message = new ValidErrorMessage(HttpStatus.BAD_REQUEST.value(), violations);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
