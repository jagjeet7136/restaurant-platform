package com.mspp.orderservice.order.service;

import com.mspp.orderservice.order.dto.request.OrderCreateRequest;
import com.mspp.orderservice.order.dto.response.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(OrderCreateRequest orderCreateRequest);
}
