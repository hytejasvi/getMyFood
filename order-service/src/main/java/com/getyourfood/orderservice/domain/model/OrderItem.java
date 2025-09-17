package com.getyourfood.orderservice.domain.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
  private UUID itemId;
  private String itemName;
  private int itemQuantity;
  private double itemPrice;
}
