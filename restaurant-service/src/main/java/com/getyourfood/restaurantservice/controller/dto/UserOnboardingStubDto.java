package com.getyourfood.restaurantservice.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserOnboardingStubDto {

  @NotNull private Long userId;

  @NotNull private String email;
}
