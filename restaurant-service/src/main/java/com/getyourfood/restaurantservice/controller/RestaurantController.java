package com.getyourfood.restaurantservice.controller;

import com.getyourfood.restaurantservice.controller.dto.MenuItemDto;
import com.getyourfood.restaurantservice.controller.dto.OnboardingRequestDto;
import com.getyourfood.restaurantservice.controller.dto.RestaurantMenuResponse;
import com.getyourfood.restaurantservice.entity.Restaurant;
import com.getyourfood.restaurantservice.service.RestaurantMenuManagementService;
import com.getyourfood.restaurantservice.service.RestaurantOwnerManagementService;
import jakarta.validation.Valid;
import java.util.List;
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

  private final RestaurantOwnerManagementService ownerManagementService;
  private final RestaurantMenuManagementService menuManagementService;

  public RestaurantController(
      RestaurantOwnerManagementService ownerManagementService,
      RestaurantMenuManagementService menuManagementService) {
    this.ownerManagementService = ownerManagementService;
    this.menuManagementService = menuManagementService;
  }

  @PostMapping("/onboarding")
  public ResponseEntity<Restaurant> completeOnboarding(
      @Valid @RequestBody OnboardingRequestDto onboardingRequestDto) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String userId = (String) auth.getPrincipal();
    Restaurant restaurant =
        ownerManagementService.completeRestaurantOnboarding(userId, onboardingRequestDto);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(restaurant);
  }

  @PostMapping("addItem")
  public ResponseEntity<RestaurantMenuResponse> addItem(
      @Valid @RequestBody List<MenuItemDto> menuItemDto) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String userId = (String) auth.getPrincipal();
    RestaurantMenuResponse response = menuManagementService.addNewItems(userId, menuItemDto);
    return ResponseEntity.ok(response);
  }
}
