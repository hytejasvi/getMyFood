package com.getyourfood.userservice.controller;

import com.getyourfood.userservice.service.exception.UnexpectedServiceException;
import com.getyourfood.userservice.service.exception.UserAlreadyRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleUserAlreadyRegisteredException(UserAlreadyRegisteredException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<?> handleUnexpectedServiceException(UnexpectedServiceException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
