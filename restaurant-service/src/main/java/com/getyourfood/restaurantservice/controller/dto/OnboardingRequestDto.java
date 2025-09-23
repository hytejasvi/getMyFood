package com.getyourfood.restaurantservice.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OnboardingRequestDto {

  @NotNull private String restaurantName;

  @NotNull private String address;

  @NotNull
  @Pattern(regexp = "\\d{6}")
  private String zipcode;

  @NotNull private String registrationNumber;
}
