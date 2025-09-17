package com.getyourfood.orderservice.domain.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
  private CustomerId customerId;
  private RestaurantId restaurantId;
  private List<OrderItem> orderItems;
  private AdditionalOrderInformation additionalOrderInformation;
  private TotalOrderAmount totalOrderAmount;
  private OrderStatus orderStatus;
}
