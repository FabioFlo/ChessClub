package org.csc.chessclub.exception.validation;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.exception.validation.messages.ValidErrorMessage;
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
  public ResponseEntity<ResponseDto<ValidErrorMessage>> validationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> violations = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((violation) -> {
      String field = ((FieldError) violation).getField();
      String message = violation.getDefaultMessage();
      violations.put(field, message);
    });
    ValidErrorMessage validationError = new ValidErrorMessage(HttpStatus.BAD_REQUEST.value(),
        violations);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ResponseDto<>(validationError, "Validation failed", false));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ResponseDto<ValidErrorMessage>> paramValidationException(
      ConstraintViolationException ex) {
    Map<String, String> violations = new HashMap<>();
    ex.getConstraintViolations().forEach(violation -> {
      String field = StreamSupport
          .stream(violation.getPropertyPath().spliterator(), false)
          .reduce((first, second) -> second)
          .map(Path.Node::getName)
          .orElse("unknown");
      String message = violation.getMessage();

      violations.put(field, message);
    });
    ValidErrorMessage validationError = new ValidErrorMessage(HttpStatus.BAD_REQUEST.value(),
        violations);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ResponseDto<>(validationError, "Validation failed", false));
  }
}
