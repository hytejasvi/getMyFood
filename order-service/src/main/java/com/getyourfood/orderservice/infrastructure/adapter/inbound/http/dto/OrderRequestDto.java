package com.getyourfood.orderservice.infrastructure.adapter.inbound.http.dto;

import com.getyourfood.orderservice.domain.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private UUID customerId;
    private UUID restaurantId;
    private List<ItemRequestDto> orderItems;
    private String additionalOrderInformation;
    private Double totalOrderAmount;
    private String orderStatus;
}

