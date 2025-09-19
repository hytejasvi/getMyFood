package com.getyourfood.userservice;

import com.getyourfood.userservice.repository.UserRepository;
import com.getyourfood.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(UserService.class)
public class UserServiceApplicationTest {

  @MockBean private UserRepository userRepository;

  @MockBean private PasswordEncoder passwordEncoder;

  @Test
  void contextLoads() {}
}
