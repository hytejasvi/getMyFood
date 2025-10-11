package com.getyourfood.restaurantservice.service.exception;

public class RestaurantOwnerNotFoundException extends ServiceException {

  public RestaurantOwnerNotFoundException(String message) {
    super(message);
  }

  public RestaurantOwnerNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
