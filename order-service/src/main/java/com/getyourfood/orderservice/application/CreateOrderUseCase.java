package com.getyourfood.orderservice.application;

import com.getyourfood.orderservice.domain.model.*;
import com.getyourfood.orderservice.infrastructure.adapter.inbound.http.dto.ItemRequestDto;
import com.getyourfood.orderservice.infrastructure.adapter.inbound.http.dto.OrderRequestDto;
import com.getyourfood.orderservice.service.CreateOrderService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateOrderUseCase {

  private final CreateOrderService createOrderService;

  public void invoke(OrderRequestDto orderRequestDto) {
    Order createOrderCommand = mapToDomainModel(orderRequestDto);
    createOrderService.invoke(createOrderCommand);
  }

  private Order mapToDomainModel(OrderRequestDto orderRequestDto) {
    List<OrderItem> domainItems = mapOrderItemsToDomain(orderRequestDto.getOrderItems());

    return new Order(
        new CustomerId(orderRequestDto.getCustomerId()),
        new RestaurantId(orderRequestDto.getRestaurantId()),
        domainItems,
        new AdditionalOrderInformation(orderRequestDto.getAdditionalOrderInformation()),
        new TotalOrderAmount(orderRequestDto.getTotalOrderAmount()),
        OrderStatus.valueOf(orderRequestDto.getOrderStatus()));
  }

  private List<OrderItem> mapOrderItemsToDomain(List<ItemRequestDto> itemRequestDto) {
    return itemRequestDto.stream()
        .map(
            itemDto ->
                new OrderItem(
                    itemDto.getItemId(),
                    itemDto.getItemName(),
                    itemDto.getItemQuantity(),
                    itemDto.getItemPrice()))
        .toList();
  }
}
