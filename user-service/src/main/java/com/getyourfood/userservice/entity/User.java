package com.getyourfood.userservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

  @Setter
  @Getter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Getter
  @Column(nullable = false)
  private String userName;

  @Setter
  @Getter
  @Column(nullable = false, unique = true)
  private String phoneNumber;

  @Setter
  @Getter
  @Column(unique = true, nullable = false)
  private String email;

  @Setter
  @Getter
  @Column(nullable = false)
  private String password;

  @Setter
  @Getter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AccountStatus status;

  @Getter
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  /*public User(
      Long id,
      String userName,
      String phoneNumber,
      String email,
      String password,
      Role role,
      AccountStatus status) {
    this.id = id;
    this.userName = userName;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.password = password;
    this.role = role;
    this.status = status;
  }*/

  // public User() {}

  /*public void setAccountStatus(AccountStatus status) {
    this.status = status;
  }*/

  /*public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }*/

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
