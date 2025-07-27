package org.csc.chessclub.exception;

import org.csc.chessclub.dto.ResponseDto;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@ControllerAdvice
@Order(2)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(CustomNotFoundException.class)
  public ResponseEntity<ResponseDto<ErrorMessage>> handleCustomNotFoundException(
      CustomNotFoundException ex) {
    ErrorMessage error = new ErrorMessage(
        ex.getMessage(), HttpStatus.NOT_FOUND.value(), Instant.now().toString());

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ResponseDto<>(error, "Not found", false));
  }

  @ExceptionHandler(CustomBadRequestException.class)
  public ResponseEntity<ResponseDto<ErrorMessage>> handleCustomBadRequestException(
      CustomBadRequestException ex) {
    ErrorMessage error = new ErrorMessage(
        ex.getMessage(), HttpStatus.BAD_REQUEST.value(), Instant.now().toString());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ResponseDto<>(error, "Bad request", false));
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ResponseDto<ErrorMessage>> handleTypeMismatch(
      MethodArgumentTypeMismatchException ex) {
    ErrorMessage error = new ErrorMessage(
        ex.getMessage(), HttpStatus.BAD_REQUEST.value(), Instant.now().toString());

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ResponseDto<>(error, "Type mismatch", false));
  }
}
