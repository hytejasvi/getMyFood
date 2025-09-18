package com.getyourfood.userservice.controller.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserSignupDto {

  @NotBlank(message = "Name cannot be blank")
  private String name;

  @Email(message = "Invalid email format")
  @NotBlank(message = "Email is required")
  private String email;

  @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
  private String phoneNumber;

  @NotBlank(message = "Password is required")
  private String password;

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getPassword() {
    return password;
  }

  public UserSignupDto(String name, String email, String phoneNumber, String password) {
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.password = password;
  }
}
