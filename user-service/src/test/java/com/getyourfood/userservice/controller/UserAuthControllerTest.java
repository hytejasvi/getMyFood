package com.getyourfood.userservice.controller;

import static com.getyourfood.userservice.fixtures.UserTestBuilder.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getyourfood.userservice.controller.dto.UserSignupDto;
import com.getyourfood.userservice.fixtures.UserTestBuilder;
import com.getyourfood.userservice.service.UserService;
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

    performSignup(Valid_SIGNUP_JSON).andExpect(status().isOk());

    verify(userService).signUp(dto);
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
