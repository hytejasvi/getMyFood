package com.getyourfood.restaurantservice.controller;

import com.getyourfood.restaurantservice.controller.dto.OnboardingRequestDto;
import com.getyourfood.restaurantservice.entity.Restaurant;
import com.getyourfood.restaurantservice.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurant/manager/")
public class RestaurantController {

  private RestaurantService restaurantService;

  public RestaurantController(RestaurantService restaurantService) {
    this.restaurantService = restaurantService;
  }

  @PostMapping("/onboarding")
  public ResponseEntity<Restaurant> completeOnboarding(
      @Valid @RequestBody OnboardingRequestDto onboardingRequestDto) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String userId = (String) auth.getPrincipal();
    Restaurant restaurant =
        restaurantService.completeRestaurantOnboarding(userId, onboardingRequestDto);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(restaurant);
  }
}
