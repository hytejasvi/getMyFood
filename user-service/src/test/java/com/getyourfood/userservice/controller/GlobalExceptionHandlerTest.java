package com.getyourfood.userservice.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.getyourfood.userservice.service.exception.UnexpectedServiceException;
import com.getyourfood.userservice.service.exception.UserAlreadyRegisteredException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

  @Test
  void should_Handle_UnexpectedServiceException() {
    UnexpectedServiceException exception =
        new UnexpectedServiceException("Test error", new RuntimeException("Cause"));

    ResponseEntity<?> response = exceptionHandler.handleUnexpectedServiceException(exception);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  void should_Handle_UnexpectedServiceException_With_Different_Messages() {
    UnexpectedServiceException exception =
        new UnexpectedServiceException(
            "Database connection failed", new RuntimeException("Connection timeout"));

    ResponseEntity<?> response = exceptionHandler.handleUnexpectedServiceException(exception);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void should_Handle_UserAlreadyRegisteredException_For_Registered_Email() {
    UserAlreadyRegisteredException exception =
        new UserAlreadyRegisteredException("Email already registered");

    ResponseEntity<?> response = exceptionHandler.handleUserAlreadyRegisteredException(exception);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void should_Handle_UserAlreadyRegisteredException_For_Registered_Phone_Number() {
    UserAlreadyRegisteredException exception =
        new UserAlreadyRegisteredException("Phone Number already registered");

    ResponseEntity<?> response = exceptionHandler.handleUserAlreadyRegisteredException(exception);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
}
