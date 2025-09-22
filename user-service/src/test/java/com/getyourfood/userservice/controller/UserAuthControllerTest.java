package com.getyourfood.userservice.controller;

import static com.getyourfood.userservice.fixtures.UserTestBuilder.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getyourfood.userservice.controller.dto.UserLoginDto;
import com.getyourfood.userservice.controller.dto.UserSignupDto;
import com.getyourfood.userservice.fixtures.UserTestBuilder;
import com.getyourfood.userservice.service.UserService;
import com.getyourfood.userservice.service.exception.UserLoginException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(UserAuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserAuthControllerTest {

  @MockBean private UserService userService;

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void shouldCreateUser_WhenValidRequest() throws Exception {

    UserSignupDto dto = new UserTestBuilder().buildSignupDto();

    performSignup(Valid_SIGNUP_JSON).andExpect(status().isCreated());

    verify(userService).signUp(dto);
  }

  @Test
  void shouldCreateRestaurantOwner_WhenValidRequest() throws Exception {

    UserSignupDto dto = new UserTestBuilder().buildSignupDto();

    performSignup(Valid_SIGNUP_JSON).andExpect(status().isCreated());

    verify(userService).signUp(dto);
  }

  @Test
  void shouldReturnToken_OnSuccessfulLogin() throws Exception {
    UserLoginDto loginDto = new UserLoginDto(DEFAULT_EMAIL, DEFAULT_PASSWORD);
    String expectedToken = "fake.jwt.token";

    when(userService.userLogin(any(UserLoginDto.class))).thenReturn(expectedToken);

    mockMvc
        .perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
        .andExpect(status().isOk())
        .andExpect(content().string(expectedToken));

    verify(userService).userLogin(loginDto);
  }

  @Test
  void shouldReturnBadRequest_OnFailedLogin() throws Exception {
    UserLoginDto loginDto = new UserLoginDto(DEFAULT_EMAIL, "wrongPassword");

    when(userService.userLogin(any(UserLoginDto.class)))
        .thenThrow(new UserLoginException("Incorrect Password"));

    mockMvc
        .perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Incorrect Password"));

    verify(userService).userLogin(loginDto);
  }

  @Test
  void should_Throw_InvalidRequest_For_Invalid_Email() throws Exception {

    performSignup(INVALID_EMAIL_SIGNUP_JSON)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").isArray())
        .andExpect(jsonPath("$.errors").value(hasItem("Invalid email format")));
  }

  @Test
  void should_Throw_InvalidRequest_For_Empty_Email() throws Exception {

    performSignup(EMPTY_EMAIL_SIGNUP_JSON)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").isArray())
        .andExpect(jsonPath("$.errors").value(hasItem("Email is required")));
  }

  @Test
  void should_Throw_InvalidRequest_For_Empty_Name() throws Exception {

    performSignup(EMPTY_NAME_SIGNUP_JSON)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").isArray())
        .andExpect(jsonPath("$.errors").value(hasItem("Name cannot be blank")));
  }

  @Test
  void should_Throw_InvalidRequest_For_Invalid_Phone_Number() throws Exception {

    performSignup(INVALID_PHONE_NUMBER_SIGNUP_JSON)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").isArray())
        .andExpect(jsonPath("$.errors").value(hasItem("Phone number must be exactly 10 digits")));
  }

  @Test
  void should_Throw_InvalidRequest_For_Empty_Name_And_Invalid_Email() throws Exception {
    performSignup(EMPTY_NAME_AND_INVALID_EMAIL_SIGNUP_JSON)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").isArray())
        .andExpect(jsonPath("$.errors", hasItems("Name cannot be blank", "Invalid email format")));
  }

  private ResultActions performSignup(String jsonContent) throws Exception {
    return mockMvc.perform(
        post("/user/signup").contentType(MediaType.APPLICATION_JSON).content(jsonContent));
  }
}
