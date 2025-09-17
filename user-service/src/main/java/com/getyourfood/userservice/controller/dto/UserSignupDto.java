package com.getyourfood.userservice.controller.dto;


import jakarta.validation.constraints.*;

public class UserSignupDto {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    private String password;

    public @NotBlank String getName() {
        return name;
    }

    public @Email @NotBlank String getEmail() {
        return email;
    }

    public @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits") @NotBlank String getPhoneNumber() {
        return phoneNumber;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public UserSignupDto(String name, String email, String phoneNumber, String password) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

}
