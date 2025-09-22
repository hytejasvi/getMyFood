package com.getyourfood.userservice.util;

import static com.getyourfood.userservice.fixtures.UserTestBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.Claims;
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

  @Test
  void shouldGenerateTokenWithClaims() {

    String token = jwtUtil.generateToken(DEFAULT_USER_ID, DEFAULT_EMAIL, DEFAULT_ROLE.toString());

    assertNotNull(token);

    Claims claims = jwtUtil.extractAllClaims(token);
    assertEquals(DEFAULT_USER_ID.toString(), claims.getSubject());
    assertEquals(DEFAULT_EMAIL, claims.get("email", String.class));
    assertEquals(DEFAULT_ROLE.toString(), claims.get("role", String.class));
    assertNotNull(claims.getIssuedAt());
    assertNotNull(claims.getExpiration());
  }

  @Test
  void shouldGenerateSimpleToken() {

    String token = jwtUtil.generateSimpleToken(DEFAULT_USER_NAME);

    assertNotNull(token);

    Claims claims = jwtUtil.extractAllClaims(token);
    assertEquals(DEFAULT_USER_NAME, claims.getSubject());
    assertNull(claims.get("email"));
    assertNull(claims.get("role"));
  }

  @Test
  void shouldExtractEmailFromToken() {

    String role = "CUSTOMER";
    String token = jwtUtil.generateToken(DEFAULT_USER_ID, DEFAULT_EMAIL, role);

    String extractedEmail = jwtUtil.extractEmail(token);

    assertEquals(extractedEmail, DEFAULT_EMAIL);
  }

  @Test
  void shouldExtractRoleFromToken() {
    String expectedRole = "RESTAURANT_OWNER";
    String token = jwtUtil.generateToken(DEFAULT_USER_ID, DEFAULT_EMAIL, expectedRole);

    String extractedRole = jwtUtil.extractRole(token);

    assertEquals(expectedRole, extractedRole);
  }

  @Test
  void shouldExtractUserIdFromToken() {
    String email = "test@example.com";
    String role = "CUSTOMER";
    String token = jwtUtil.generateToken(DEFAULT_USER_ID, email, role);

    Long extractedUserId = Long.valueOf(jwtUtil.extractUserId(token));

    assertEquals(DEFAULT_USER_ID, extractedUserId);
  }

  @Test
  void shouldReturnFalseForValidToken() {
    String token = jwtUtil.generateSimpleToken(DEFAULT_USER_NAME);

    assertFalse(jwtUtil.isTokenExpired(token));
  }
}
