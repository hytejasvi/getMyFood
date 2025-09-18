package com.getyourfood.userservice.service;

import static com.getyourfood.userservice.fixtures.UserTestBuilder.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.getyourfood.userservice.controller.dto.UserSignupDto;
import com.getyourfood.userservice.entity.User;
import com.getyourfood.userservice.fixtures.UserTestBuilder;
import com.getyourfood.userservice.repository.UserRepository;
import com.getyourfood.userservice.service.exception.UserAlreadyRegisteredException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private UserService userService;

  @BeforeEach
  void setup() {}

  @Test
  void shouldSaveValidNewUser() {
    UserSignupDto userSignupDto = new UserTestBuilder().buildSignupDto();

    when(userRepository.findByEmailOrPhoneNumber(any(), any())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(any())).thenReturn(DEFAULT_ENCODED_PASSWORD);

    userService.signUp(userSignupDto);

    verify(userRepository, times(1))
        .save(
            argThat(
                user ->
                    user.getEmail().equals(userSignupDto.getEmail())
                        && user.getPhoneNumber().equals(userSignupDto.getPhoneNumber())
                        && user.getUserName().equals(userSignupDto.getName())
                        && user.getPassword().equals(DEFAULT_ENCODED_PASSWORD)));
  }

  @Test
  void should_throw_UserAlreadyRegisteredException_For_Existing_Email() {
    UserSignupDto userSignupDto = new UserTestBuilder().buildSignupDto();
    User user = new UserTestBuilder().buildUserEntity();

    when(userRepository.findByEmailOrPhoneNumber(DEFAULT_EMAIL, DEFAULT_PHONE_NUMBER))
        .thenReturn(Optional.ofNullable(user));

    UserAlreadyRegisteredException ex =
        assertThrows(UserAlreadyRegisteredException.class, () -> userService.signUp(userSignupDto));

    assertEquals(("Email already registered"), ex.getMessage());
  }

  @Test
  void should_throw_UserAlreadyRegisteredException_For_Existing_Phone_Number() {
    String newEmail = "test1@example.com";
    UserSignupDto userSignupDto = new UserTestBuilder().withEmail(newEmail).buildSignupDto();
    User user = new UserTestBuilder().buildUserEntity();

    when(userRepository.findByEmailOrPhoneNumber(newEmail, DEFAULT_PHONE_NUMBER))
        .thenReturn(Optional.ofNullable(user));

    UserAlreadyRegisteredException ex =
        assertThrows(UserAlreadyRegisteredException.class, () -> userService.signUp(userSignupDto));

    assertEquals("Phone Number already registered", ex.getMessage());
  }
}
