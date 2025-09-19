package com.getyourfood.userservice.service;

import com.getyourfood.userservice.controller.dto.UserSignupDto;
import com.getyourfood.userservice.entity.User;
import com.getyourfood.userservice.repository.UserRepository;
import com.getyourfood.userservice.service.exception.UserAlreadyRegisteredException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public void signUp(UserSignupDto dto) {
    validateUniqueUser(dto);
    User user = mapToUser(dto);
    userRepository.save(user);
  }

  private User mapToUser(UserSignupDto dto) {
    User user = new User();
    user.setEmail(dto.getEmail());
    user.setUserName(dto.getName());
    user.setPhoneNumber(dto.getPhoneNumber());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    return user;
  }

  private void validateUniqueUser(UserSignupDto dto) {
    userRepository
        .findByEmailOrPhoneNumber(dto.getEmail(), dto.getPhoneNumber())
        .ifPresent(
            existingUser -> {
              if (existingUser.getEmail().equals(dto.getEmail())) {
                throw new UserAlreadyRegisteredException("Email already registered");
              }
              if (existingUser.getPhoneNumber().equals(dto.getPhoneNumber())) {
                throw new UserAlreadyRegisteredException("Phone Number already registered");
              }
            });
  }
}
