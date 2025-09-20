package com.getyourfood.userservice.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {

  private JwtUtil jwtUtil;

  private final String testSecret = "testSecretKeyWhichIsLongEnoughForHS256123456";
  private final Long expiration = 86400000L; // 1 day

  @BeforeEach
  void setUp() {
    jwtUtil = new JwtUtil();
    ReflectionTestUtils.setField(jwtUtil, "secret", testSecret);
    ReflectionTestUtils.setField(jwtUtil, "EXPIRATION_TIME", expiration);
  }

  @Test
  void shouldGenerateValidToken() {
    String token = jwtUtil.generateToken(123L, "test@example.com", "CUSTOMER");
    assertNotNull(token);
    assertEquals("123", jwtUtil.extractUserId(token));
  }

  @Test
  void shouldValidateToken_ForCorrectUserId() {
    String token = jwtUtil.generateToken(123L, "test@example.com", "CUSTOMER");

    assertTrue(jwtUtil.validateToken(token, "123"));
  }

  @Test
  void shouldNotValidateToken_ForIncorrectUserId() {
    String token = jwtUtil.generateToken(123L, "test@example.com", "CUSTOMER");

    assertFalse(jwtUtil.validateToken(token, "999"));
  }
}
