package com.getyourfood.orderservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private UUID itemId;
    private String itemName;
    private int itemQuantity;
    private double itemPrice;
}
