package com.getyourfood.userservice.service;

import static com.getyourfood.userservice.fixtures.UserTestBuilder.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.getyourfood.userservice.controller.dto.UserLoginDto;
import com.getyourfood.userservice.controller.dto.UserSignupDto;
import com.getyourfood.userservice.entity.Role;
import com.getyourfood.userservice.entity.User;
import com.getyourfood.userservice.fixtures.UserTestBuilder;
import com.getyourfood.userservice.repository.UserRepository;
import com.getyourfood.userservice.service.exception.UnexpectedServiceException;
import com.getyourfood.userservice.service.exception.UserAlreadyRegisteredException;
import com.getyourfood.userservice.service.exception.UserLoginException;
import com.getyourfood.userservice.util.JwtUtil;
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

  @Mock private JwtUtil jwtUtil;

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
  void shouldSaveValidNewRestaurantOwner() {
    UserSignupDto userSignupDto = new UserTestBuilder().buildSignupDto();

    when(userRepository.findByEmailOrPhoneNumber(any(), any())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(any())).thenReturn(DEFAULT_ENCODED_PASSWORD);

    userService.signUpRestaurantOwner(userSignupDto);

    verify(userRepository, times(1))
        .save(
            argThat(
                user ->
                    user.getEmail().equals(userSignupDto.getEmail())
                        && user.getPhoneNumber().equals(userSignupDto.getPhoneNumber())
                        && user.getUserName().equals(userSignupDto.getName())
                        && user.getPassword().equals(DEFAULT_ENCODED_PASSWORD)
                        && user.getRole().equals(Role.valueOf("RESTAURANT_OWNER"))));
  }

  @Test
  void shouldReturnToken_ForValidCredentials() {

    UserLoginDto loginDto = new UserTestBuilder().buildLoginDto();
    User existingUser = new UserTestBuilder().buildUserEntity();

    when(userRepository.findByEmailOrPhoneNumber(DEFAULT_PHONE_NUMBER, DEFAULT_PHONE_NUMBER))
        .thenReturn(Optional.of(existingUser));
    when(passwordEncoder.matches(DEFAULT_PASSWORD, DEFAULT_ENCODED_PASSWORD)).thenReturn(true);
    when(jwtUtil.generateToken(anyLong(), anyString(), anyString())).thenReturn("fake.jwt.token");

    String result = userService.userLogin(loginDto);

    assertNotNull(result);
    assertEquals("fake.jwt.token", result);
    verify(jwtUtil)
        .generateToken(
            existingUser.getId(), existingUser.getEmail(), existingUser.getRole().toString());
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

  @Test
  void shouldThrowUnexpectedServiceException_WhenRepositoryFailsDuringSignUp() {
    UserSignupDto userSignupDto = new UserTestBuilder().buildSignupDto();

    when(userRepository.findByEmailOrPhoneNumber(any(), any())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(any())).thenReturn(DEFAULT_ENCODED_PASSWORD);
    when(userRepository.save(any(User.class)))
        .thenThrow(new RuntimeException("Database connection failed"));

    UnexpectedServiceException ex =
        assertThrows(UnexpectedServiceException.class, () -> userService.signUp(userSignupDto));

    assertEquals("Database connection failed", ex.getMessage());
    assertNotNull(ex.getCause());
  }

  @Test
  void shouldThrowLoginException_ForInvalidUsername() {
    UserLoginDto loginDto = new UserLoginDto("wrong@email.com", DEFAULT_PASSWORD);

    when(userRepository.findByEmailOrPhoneNumber("wrong@email.com", "wrong@email.com"))
        .thenReturn(Optional.empty());

    UserLoginException ex =
        assertThrows(UserLoginException.class, () -> userService.userLogin(loginDto));

    assertEquals("Invalid Login details", ex.getMessage());
  }

  @Test
  void shouldThrowLoginException_ForInvalidPassword() {
    UserLoginDto loginDto = new UserLoginDto(DEFAULT_EMAIL, "wrongPassword");
    User existingUser = new UserTestBuilder().buildUserEntity();

    when(userRepository.findByEmailOrPhoneNumber(DEFAULT_EMAIL, DEFAULT_EMAIL))
        .thenReturn(Optional.of(existingUser));
    when(passwordEncoder.matches("wrongPassword", DEFAULT_ENCODED_PASSWORD)).thenReturn(false);

    UserLoginException ex =
        assertThrows(UserLoginException.class, () -> userService.userLogin(loginDto));

    assertEquals("Incorrect Password", ex.getMessage());
  }
}
