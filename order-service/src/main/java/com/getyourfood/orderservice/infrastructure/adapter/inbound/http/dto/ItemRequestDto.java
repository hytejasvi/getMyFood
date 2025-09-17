package com.getyourfood.orderservice.infrastructure.adapter.inbound.http.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
  private UUID itemId;
  private String itemName;
  private int itemQuantity;
  private double itemPrice;
}
