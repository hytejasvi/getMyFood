package com.getyourfood.restaurantservice.service;

import com.getyourfood.restaurantservice.controller.dto.OnboardingRequestDto;
import com.getyourfood.restaurantservice.controller.dto.UserOnboardingStubDto;
import com.getyourfood.restaurantservice.entity.Restaurant;
import com.getyourfood.restaurantservice.repository.RestaurantRepository;
import com.getyourfood.restaurantservice.service.exception.RestaurantOwnerNotFoundException;
import com.getyourfood.restaurantservice.service.exception.UnexpectedServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class RestaurantOwnerManagementService {

  private final RestaurantRepository restaurantRepository;

  public RestaurantOwnerManagementService(RestaurantRepository restaurantRepository) {
    this.restaurantRepository = restaurantRepository;
  }

  public void createRestaurantStub(UserOnboardingStubDto onboardingStubDto) {
    try {
      boolean restaurantExists = restaurantRepository.existsById(onboardingStubDto.getUserId());

      if (!restaurantExists) {
        Restaurant restaurant = mapToEntity(onboardingStubDto);
        log.info(
            "Creating new restaurant onboarding stub for user: {}", onboardingStubDto.getUserId());
        restaurantRepository.save(restaurant);
      } else {
        log.warn(
            "The Restaurant Stub is already created for user: {}", onboardingStubDto.getEmail());
      }
    } catch (Exception e) {
      handleException(String.valueOf(onboardingStubDto.getUserId()), e);
    }
  }

  @Transactional
  public Restaurant completeRestaurantOnboarding(
      String existingUserId, OnboardingRequestDto requestDto) {
    try {
      Long userId = Long.parseLong(existingUserId);

      if (!restaurantRepository.existsById(userId)) {
        log.error("Restaurant not found for user: {}", userId);
        throw new RestaurantOwnerNotFoundException("Restaurant Owner is not registered");
      }

      Restaurant restaurant = restaurantRepository.getReferenceById(userId);
      Restaurant updatedRestaurant = mapToRestaurantEntity(restaurant, requestDto);

      return restaurantRepository.save(updatedRestaurant);

    } catch (Exception e) {
      handleException(existingUserId, e);
      return null;
    }
  }

  private Restaurant mapToRestaurantEntity(
      Restaurant existingRestaurantOpt, OnboardingRequestDto requestDto) {
    existingRestaurantOpt.setName(requestDto.restaurantName());
    existingRestaurantOpt.setAddress(requestDto.address());
    existingRestaurantOpt.setRegistrationNumber(requestDto.registrationNumber());
    existingRestaurantOpt.setZipcode(requestDto.zipcode());
    existingRestaurantOpt.setStatus(Restaurant.OnboardingStatus.ACTIVE);
    return existingRestaurantOpt;
  }

  private Restaurant mapToEntity(UserOnboardingStubDto onboardingStubDto) {
    Restaurant restaurant = new Restaurant();
    restaurant.setId(onboardingStubDto.getUserId());
    restaurant.setEmail(onboardingStubDto.getEmail());
    restaurant.setStatus(Restaurant.OnboardingStatus.PENDING_ONBOARDING);
    return restaurant;
  }

  private void handleException(String userId, Exception e) {
    if (e instanceof DataAccessException) {
      log.error("Database error for user {}: {}", userId, e.getMessage(), e);
      throw new UnexpectedServiceException("Service temporarily unavailable", e);
    } else if (e instanceof IllegalArgumentException) {
      log.error("Invalid argument for user {}: {}", userId, e.getMessage(), e);
      throw new UnexpectedServiceException("Invalid user ID format");
    } else {
      log.error("Unexpected error for user {}: {}", userId, e.getMessage(), e);
      throw new UnexpectedServiceException("An unexpected error occurred");
    }
  }
}
