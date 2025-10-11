package com.getyourfood.restaurantservice.service.exception;

public class UnexpectedServiceException extends ServiceException {
  public UnexpectedServiceException(String message) {
    super(message);
  }

  public UnexpectedServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
