package com.getyourfood.userservice.controller;

import com.getyourfood.userservice.service.exception.UnexpectedServiceException;
import com.getyourfood.userservice.service.exception.UserAlreadyRegisteredException;
import com.getyourfood.userservice.service.exception.UserLoginException;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<ErrorResponseDto> handleValidationException(
      MethodArgumentNotValidException e) {
    List<String> errors =
        e.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
    var errorResponseDto = ErrorResponseDto.builder().errors(errors).build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
  }

  @ExceptionHandler
  public ResponseEntity<?> handleUserAlreadyRegisteredException(UserAlreadyRegisteredException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<?> handleUserLoginException(UserLoginException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<?> handleUnexpectedServiceException(UnexpectedServiceException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }

  @Data
  @Builder
  private static class ErrorResponseDto {
    private List<String> errors;
  }
}
