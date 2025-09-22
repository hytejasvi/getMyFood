package com.getyourfood.userservice.service;

import com.getyourfood.userservice.controller.dto.UserLoginDto;
import com.getyourfood.userservice.controller.dto.UserSignupDto;
import com.getyourfood.userservice.entity.AccountStatus;
import com.getyourfood.userservice.entity.Role;
import com.getyourfood.userservice.entity.User;
import com.getyourfood.userservice.repository.UserRepository;
import com.getyourfood.userservice.service.exception.UnexpectedServiceException;
import com.getyourfood.userservice.service.exception.UserAlreadyRegisteredException;
import com.getyourfood.userservice.service.exception.UserLoginException;
import com.getyourfood.userservice.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final JwtUtil jwtUtil;

  public UserService(
      UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  public void signUp(UserSignupDto dto) {
    try {
      validateUniqueUser(dto);
      User user = mapToUser(dto);
      userRepository.save(user);
    } catch (UserAlreadyRegisteredException e) {
      throw e;
    } catch (Exception e) {
      throw new UnexpectedServiceException(e.getMessage(), e);
    }
  }

  public void signUpRestaurantOwner(UserSignupDto dto) {
    try {
      validateUniqueUser(dto);
      User user = mapToRestaurantOwner(dto);
      userRepository.save(user);
    } catch (UserAlreadyRegisteredException e) {
      throw e;
    } catch (Exception e) {
      throw new UnexpectedServiceException(e.getMessage(), e);
    }
  }

  public String userLogin(UserLoginDto loginDto) {

    User user = validateLoginId(loginDto.getLoginId());

    if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
      throw new UserLoginException("Incorrect Password");
    }

    String tokens = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().toString());
    log.info("Generated tokens: {}", tokens);
    return tokens;
  }

  private User validateLoginId(String loginId) {
    return userRepository
        .findByEmailOrPhoneNumber(loginId, loginId)
        .orElseThrow(() -> new UserLoginException("Invalid Login details"));
  }

  private User mapToUser(UserSignupDto dto) {
    User user = new User();
    user.setEmail(dto.getEmail());
    user.setUserName(dto.getName());
    user.setPhoneNumber(dto.getPhoneNumber());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setRole(Role.CUSTOMER);
    user.setStatus(AccountStatus.ACTIVE);
    return user;
  }

  private User mapToRestaurantOwner(UserSignupDto dto) {
    User user = new User();
    user.setEmail(dto.getEmail());
    user.setUserName(dto.getName());
    user.setPhoneNumber(dto.getPhoneNumber());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setRole(Role.RESTAURANT_OWNER);
    user.setStatus(AccountStatus.VERIFICATION_PENDING);
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
