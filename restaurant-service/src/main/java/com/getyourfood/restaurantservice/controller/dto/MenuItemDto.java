package com.getyourfood.restaurantservice.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MenuItemDto {

  @NotNull(message = "Item name is required")
  @Size(min = 2, max = 100, message = "Item name must be between 2 and 100 characters")
  private String name;

  @NotNull(message = "Item price is required")
  @Positive(message = "Price must be greater than 0")
  @DecimalMin(value = "0.01", message = "Price must be greater than 0.01")
  private Double price;

  @NotNull(message = "Category is required")
  private String category;

  @Size(max = 1000, message = "Description cannot exceed 1000 characters")
  private String description = "";

  private String imageUrl = "";

  private String status =
      "NOT_AVAILABLE"; // default to NOT_AVAILABLE if nothing is provided in input
}
