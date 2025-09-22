package com.getyourfood.userservice.controller;

import com.getyourfood.userservice.controller.dto.UserLoginDto;
import com.getyourfood.userservice.controller.dto.UserSignupDto;
import com.getyourfood.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Validated
public class UserAuthController {

  private final UserService userService;

  public UserAuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/signup")
  public ResponseEntity<?> createUser(@Valid @RequestBody UserSignupDto userSignupDto) {
    userService.signUp(userSignupDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/restaurant-owner/signup")
  public ResponseEntity<?> createRestaurantOwner(@Valid @RequestBody UserSignupDto userSignupDto) {
    userService.signUpRestaurantOwner(userSignupDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/login")
  public ResponseEntity<?> userLogin(@Valid @RequestBody UserLoginDto userLoginDto) {
    String tokens = userService.userLogin(userLoginDto);
    return ResponseEntity.status(HttpStatus.OK).body(tokens);
  }
}
