package com.getyourfood.restaurantservice.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record OnboardingRequestDto(
    @NotNull String restaurantName,
    @NotNull String address,
    @NotNull @Pattern(regexp = "\\d{6}") String zipcode,
    @NotNull String registrationNumber) {}
