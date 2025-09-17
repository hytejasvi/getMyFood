package com.getyourfood.orderservice.service;

import com.getyourfood.orderservice.domain.model.Order;
import com.getyourfood.orderservice.domain.port.CreateOrderPort;
import org.springframework.stereotype.Service;

@Service
public class CreateOrderService {

  private final CreateOrderPort createOrderPort;

  public CreateOrderService(CreateOrderPort createOrderPort) {
    this.createOrderPort = createOrderPort;
  }

  public void invoke(Order newOrder) {
    createOrderPort.createOrder(newOrder);
  }
}
