package com.getyourfood.userservice.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

  @NotBlank(message = "loginId must not be blank")
  private String loginId;

  @NotBlank(message = "password must not be blank")
  private String password;
}
