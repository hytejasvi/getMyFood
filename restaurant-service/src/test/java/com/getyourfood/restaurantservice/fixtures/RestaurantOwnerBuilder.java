package com.getyourfood.restaurantservice.fixtures;

import com.getyourfood.restaurantservice.controller.dto.OnboardingRequestDto;
import com.getyourfood.restaurantservice.entity.Restaurant;

public class RestaurantOwnerBuilder {

  public static final Long DEFAULT_USER_ID = 1L;
  public static final String DEFAULT_EMAIL_ID = "owner1@gmail.com";
  private static final String DEFAULT_RESTAURANT_NAME = "restaurant1";
  private static final String DEFAULT_RESTAURANT_ADDRESS =
      "current nowhere, will setup somewhere soon!!";
  private static final String DEFAULT_RESTAURANT_ZIPCODE = "650063";
  private static final String DEFAULT_RESTAURANT_REGISTRATION_NUMBER = "REG0001";

  public static OnboardingRequestDto buildOnboardingRequestDto() {
    return new OnboardingRequestDto(
        DEFAULT_RESTAURANT_NAME,
        DEFAULT_RESTAURANT_ADDRESS,
        DEFAULT_RESTAURANT_ZIPCODE,
        DEFAULT_RESTAURANT_REGISTRATION_NUMBER);
  }

  public static Restaurant buildRestaurantEntity() {
    Restaurant restaurant = new Restaurant();
    restaurant.setId(DEFAULT_USER_ID);
    restaurant.setEmail(DEFAULT_EMAIL_ID);
    restaurant.setStatus(Restaurant.OnboardingStatus.PENDING_ONBOARDING);
    return restaurant;
  }

  public static Restaurant buildOnboardedRestaurantEntity() {
    Restaurant restaurant = new Restaurant();
    restaurant.setId(DEFAULT_USER_ID);
    restaurant.setEmail(DEFAULT_EMAIL_ID);
    restaurant.setName(DEFAULT_RESTAURANT_NAME);
    restaurant.setAddress(DEFAULT_RESTAURANT_ADDRESS);
    restaurant.setZipcode(DEFAULT_RESTAURANT_ZIPCODE);
    restaurant.setRegistrationNumber(DEFAULT_RESTAURANT_REGISTRATION_NUMBER);
    restaurant.setStatus(Restaurant.OnboardingStatus.ACTIVE);
    return restaurant;
  }
}
