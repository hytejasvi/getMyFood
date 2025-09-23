package com.getyourfood.restaurantservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "menu_items")
public class MenuItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Category category;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ItemStatus status;

  @Column(length = 1000)
  private String description;

  @Column(nullable = false)
  private double price;

  private String imageUrl; // Optional field for item image

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  // Many menu items belong to one restaurant
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "restaurant_id", nullable = false)
  private Restaurant restaurant;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  public enum Category {
    APPETIZER,
    MAIN_COURSE,
    DESSERT,
    BEVERAGE,
    SOUP,
    SALAD,
    SIDE_DISH,
    BREAKFAST,
    LUNCH,
    DINNER,
  }

  public enum ItemStatus {
    AVAILABLE,
    NOT_AVAILABLE,
  }
}
