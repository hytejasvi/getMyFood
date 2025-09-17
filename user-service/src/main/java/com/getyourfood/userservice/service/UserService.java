package com.getyourfood.userservice.service;

import com.getyourfood.userservice.controller.dto.UserSignupDto;
import com.getyourfood.userservice.entity.User;
import com.getyourfood.userservice.repository.UserRepository;
import com.getyourfood.userservice.service.exception.UnexpectedServiceException;
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

  public void signUp(UserSignupDto userSignupDto) {
    try {
      if (!checkIfRegisteredUser(userSignupDto)) {
        User user = buildUser(userSignupDto);
        userRepository.save(user);
      }
    } catch (Exception e) {
      throw new UnexpectedServiceException(e.getMessage(), e);
    }
  }

  private User buildUser(UserSignupDto userSignupDto) {
    User user = new User();
    user.setEmail(userSignupDto.getEmail());
    user.setUserName(userSignupDto.getName());
    user.setPhoneNumber(userSignupDto.getPhoneNumber());
    user.setPassword(passwordEncoder.encode(userSignupDto.getPassword()));
    return user;
  }

  private Boolean checkIfRegisteredUser(UserSignupDto userSignupDto) {
    String email = userSignupDto.getEmail();
    String phoneNumber = userSignupDto.getPhoneNumber();

    userRepository
        .findByEmailOrPhoneNumber(email, phoneNumber)
        .ifPresent(
            existingUser -> {
              if (existingUser.getEmail().equals(email)) {
                throw new UserAlreadyRegisteredException("Email already registered");
              }
              if (existingUser.getPhoneNumber().equals(phoneNumber)) {
                throw new UserAlreadyRegisteredException("Phone Number already registered");
              }
            });
    return false;
  }
}
