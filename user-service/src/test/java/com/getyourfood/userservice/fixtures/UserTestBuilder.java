package com.getyourfood.userservice.fixtures;

import com.getyourfood.userservice.controller.dto.UserLoginDto;
import com.getyourfood.userservice.controller.dto.UserSignupDto;
import com.getyourfood.userservice.entity.AccountStatus;
import com.getyourfood.userservice.entity.Role;
import com.getyourfood.userservice.entity.User;

public class UserTestBuilder {

  public static final Long DEFAULT_USER_ID = 1L;
  public static final String DEFAULT_USER_NAME = "Test User";
  public static final String DEFAULT_PASSWORD = "password";
  public static final String DEFAULT_EMAIL = "test@example.com";
  public static final String DEFAULT_PHONE_NUMBER = "1234567890";
  public static final String DEFAULT_ENCODED_PASSWORD = "encodedPassword123";
  public static final Role DEFAULT_ROLE = Role.valueOf("CUSTOMER");
  public static final AccountStatus DEFAULT_STATUS = AccountStatus.valueOf("ACTIVE");

  private String userName = DEFAULT_USER_NAME;
  private String password = DEFAULT_PASSWORD;
  private String email = DEFAULT_EMAIL;
  private String phoneNumber = DEFAULT_PHONE_NUMBER;

  public UserTestBuilder withUserName(String userName) {
    this.userName = userName;
    return this;
  }

  public UserTestBuilder withPassword(String password) {
    this.password = password;
    return this;
  }

  public UserTestBuilder withEmail(String email) {
    this.email = email;
    return this;
  }

  public UserTestBuilder withPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  public UserSignupDto buildSignupDto() {
    return new UserSignupDto(userName, email, phoneNumber, password);
  }

  public UserLoginDto buildLoginDto() {
    return new UserLoginDto(phoneNumber, password);
  }

  public User buildUserEntity() {
    User user = new User();
    user.setId(DEFAULT_USER_ID);
    user.setEmail(email);
    user.setUserName(userName);
    user.setPhoneNumber(phoneNumber);
    user.setPassword(DEFAULT_ENCODED_PASSWORD);
    user.setRole(DEFAULT_ROLE);
    user.setStatus(DEFAULT_STATUS);
    return user;
  }

  public static final String Valid_SIGNUP_JSON =
      """
            {
              "name": "Test User",
              "email": "test@example.com",
              "phoneNumber": "1234567890",
              "password": "password"
            }
            """;

  public static final String INVALID_EMAIL_SIGNUP_JSON =
      """
            {
              "name": "Test User",
              "email": "@example.com",
              "phoneNumber": "1234567890",
              "password": "password"
            }
            """;

  public static final String EMPTY_EMAIL_SIGNUP_JSON =
      """
            {
              "name": "Test User",
              "email": "",
              "phoneNumber": "1234567890",
              "password": "password"
            }
            """;

  public static final String EMPTY_NAME_SIGNUP_JSON =
      """
            {
              "name": "",
              "email": "test@example.com",
              "phoneNumber": "1234567890",
              "password": "password"
            }
            """;

  public static final String INVALID_PHONE_NUMBER_SIGNUP_JSON =
      """
            {
              "name": "Test User",
              "email": "test@example.com",
              "phoneNumber": "",
              "password": "password"
            }
            """;

  public static final String EMPTY_NAME_AND_INVALID_EMAIL_SIGNUP_JSON =
      """
            {
              "name": "",
              "email": "invalid-email",
              "phoneNumber": "1234567890",
              "password": "password"
            }""";
}
