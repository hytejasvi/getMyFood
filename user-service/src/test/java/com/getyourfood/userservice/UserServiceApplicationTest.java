package com.getyourfood.userservice;

import com.getyourfood.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class UserServiceApplicationTest {

  @MockBean private UserRepository userRepository;

  @Test
  void contextLoads() {}
}
