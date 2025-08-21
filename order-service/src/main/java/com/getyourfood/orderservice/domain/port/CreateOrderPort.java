package com.getyourfood.orderservice.domain.port;

import com.getyourfood.orderservice.domain.model.Order;

public interface CreateOrderPort {
    void createOrder(Order order);
}
