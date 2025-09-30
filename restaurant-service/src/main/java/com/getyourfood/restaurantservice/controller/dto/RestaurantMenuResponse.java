package com.getyourfood.restaurantservice.controller.dto;

import java.util.List;

public record RestaurantMenuResponse(String restaurantName, List<MenuItemResponse> menuItems) {}
