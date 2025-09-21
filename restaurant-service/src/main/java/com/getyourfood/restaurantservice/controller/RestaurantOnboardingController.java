package com.getyourfood.restaurantservice.controller;

import com.getyourfood.restaurantservice.controller.dto.UserOnboardingStubDto;
import com.getyourfood.restaurantservice.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/restaurants")
public class RestaurantOnboardingController {

    private final RestaurantService restaurantService;

    public RestaurantOnboardingController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping("/onboarding-stub")
    public ResponseEntity<HttpStatus> createOnboardingStub(
            @Valid
            @RequestBody UserOnboardingStubDto onboardingStubDto
    ) {
        restaurantService.createRestaurantStub(onboardingStubDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
