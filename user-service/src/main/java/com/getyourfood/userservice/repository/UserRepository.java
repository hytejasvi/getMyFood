package com.getyourfood.userservice.repository;

import com.getyourfood.userservice.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);
}
