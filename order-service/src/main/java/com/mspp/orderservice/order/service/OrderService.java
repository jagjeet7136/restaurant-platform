package com.mspp.orderservice.order.service;

import com.mspp.orderservice.order.dto.request.OrderCreateRequest;
import com.mspp.orderservice.order.dto.response.OrderResponse;
import com.mspp.orderservice.order.entity.OrderStatus;

public interface OrderService {

    OrderResponse createOrder(OrderCreateRequest orderCreateRequest);
    OrderResponse getOrderById(Long id);
    OrderResponse updateOrderStatus(Long id, OrderStatus newStatus);
}
