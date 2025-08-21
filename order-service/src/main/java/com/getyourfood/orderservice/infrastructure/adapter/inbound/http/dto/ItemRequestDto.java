package com.getyourfood.orderservice.infrastructure.adapter.inbound.http.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private UUID itemId;
    private String itemName;
    private int itemQuantity;
    private double itemPrice;
}
