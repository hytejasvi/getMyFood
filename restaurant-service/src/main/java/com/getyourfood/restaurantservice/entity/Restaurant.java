package com.getyourfood.restaurantservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "restaurants")
@Data
public class Restaurant {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OnboardingStatus status;

    private String name;
    private String address;
    private String registrationNumber;
    private String phoneNumber;
    private String zipcode;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // One restaurant can have many menu items
    // 'restaurant' is the name of the field in the MenuItem class that owns the relationship
    /*@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MenuItem> menuItems;*/

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum OnboardingStatus {
        PENDING_ONBOARDING,
        ACTIVE,
        REJECTED
    }

}
