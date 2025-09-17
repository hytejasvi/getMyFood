package com.getyourfood.userservice.service.exception;

public class UserAlreadyRegisteredException extends ServiceException {
  public UserAlreadyRegisteredException(String message) {
    super(message);
  }

  public UserAlreadyRegisteredException(String message, Throwable cause) {
    super(message, cause);
  }
}
