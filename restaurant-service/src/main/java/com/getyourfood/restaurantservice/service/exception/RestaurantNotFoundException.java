package com.getyourfood.restaurantservice.service.exception;

public class RestaurantNotFoundException extends ServiceException {
  public RestaurantNotFoundException(String message) {
    super(message);
  }
}
