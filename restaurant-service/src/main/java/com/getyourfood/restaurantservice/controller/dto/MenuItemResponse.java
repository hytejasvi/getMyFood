package com.getyourfood.restaurantservice.controller.dto;

public record MenuItemResponse(
    Long id,
    String itemName,
    double price,
    String category,
    String status,
    String description,
    String imageUrl) {}
