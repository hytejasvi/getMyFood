package com.getyourfood.restaurantservice.service;

import com.getyourfood.restaurantservice.controller.dto.OnboardingRequestDto;
import com.getyourfood.restaurantservice.controller.dto.UserOnboardingStubDto;
import com.getyourfood.restaurantservice.entity.Restaurant;
import com.getyourfood.restaurantservice.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }


    public void createRestaurantStub(UserOnboardingStubDto onboardingStubDto) {
        Optional<Restaurant> existingRestaurant = restaurantRepository.findById(onboardingStubDto.getUserId());
        if(existingRestaurant.isPresent()) {
            log.info("Restaurant stub already exists for userId: {}", onboardingStubDto.getUserId());
            return;
        }
        Restaurant restaurant = mapToEntity(onboardingStubDto);
        log.info("Creating new restaurant onboarding stub for user: {}", onboardingStubDto.getUserId());
        restaurantRepository.save(restaurant);
    }

    public Restaurant completeRestaurantOnboarding(String existingUserId, OnboardingRequestDto requestDto) {
        Long userId = Long.parseLong(existingUserId);
        Optional<Restaurant> existingRestaurantOpt  = restaurantRepository.findById(userId);
        if(existingRestaurantOpt.isPresent()) {
            Restaurant restaurant = mapToRestaurantEntity(existingRestaurantOpt.get(), requestDto);
            Restaurant savedRestaurant = restaurantRepository.save(restaurant);
            return savedRestaurant;
        }
        return null;
    }

    private Restaurant mapToRestaurantEntity(Restaurant existingRestaurantOpt, OnboardingRequestDto requestDto) {
        existingRestaurantOpt.setName(requestDto.getRestaurantName());
        existingRestaurantOpt.setAddress(requestDto.getAddress());
        existingRestaurantOpt.setRegistrationNumber(requestDto.getRegistrationNumber());
        existingRestaurantOpt.setZipcode(requestDto.getZipcode());
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
}
