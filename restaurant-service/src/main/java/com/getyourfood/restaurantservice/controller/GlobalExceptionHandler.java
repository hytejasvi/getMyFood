package com.getyourfood.restaurantservice.controller;

import com.getyourfood.restaurantservice.service.exception.RestaurantNotFoundException;
import com.getyourfood.restaurantservice.service.exception.RestaurantOwnerNotFoundException;
import com.getyourfood.restaurantservice.service.exception.UnexpectedServiceException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    String errors =
        e.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining(", "));

    ErrorResponseDto responseDto = ErrorResponseDto.of("Validation Error", errors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
  }

  @ExceptionHandler(RestaurantOwnerNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleRestaurantOwnerNotFoundException(
      RestaurantOwnerNotFoundException e) {
    ErrorResponseDto responseDto = ErrorResponseDto.of("Restaurant Not Found", e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
  }

  @ExceptionHandler(RestaurantNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleRestaurantNotFoundException(
      RestaurantNotFoundException e) {
    ErrorResponseDto responseDto = ErrorResponseDto.builder().message(e.getMessage()).build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
  }

  @ExceptionHandler(UnexpectedServiceException.class)
  public ResponseEntity<ErrorResponseDto> handleUnexpectedServiceException(
      UnexpectedServiceException e) {
    ErrorResponseDto responseDto = ErrorResponseDto.of("Service Error", e.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
  }
}
