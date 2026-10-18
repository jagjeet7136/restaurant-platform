package com.mspp.orderservice.order.service.impl;

import com.mspp.orderservice.client.restaurant.RestaurantClient;
import com.mspp.orderservice.client.restaurant.RestaurantClientResponse;
import com.mspp.orderservice.order.dto.request.OrderCreateRequest;
import com.mspp.orderservice.order.dto.response.OrderResponse;
import com.mspp.orderservice.order.entity.Order;
import com.mspp.orderservice.order.entity.OrderStatus;
import com.mspp.orderservice.order.repository.OrderRepository;
import com.mspp.orderservice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantClient restaurantClient;

    @Override
    public OrderResponse createOrder(OrderCreateRequest request) {

        RestaurantClientResponse restaurant =
                restaurantClient.getRestaurantById(request.getRestaurantId());

        if (!Boolean.TRUE.equals(restaurant.getActive())) {
            throw new IllegalArgumentException(
                    "Cannot place an order for an inactive restaurant"
            );
        }

        Order order = new Order();
        order.setRestaurantId(restaurant.getId());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(request.getTotalAmount());

        Order savedOrder = orderRepository.save(order);

        return OrderResponse.builder()
                .id(savedOrder.getId())
                .restaurantId(savedOrder.getRestaurantId())
                .status(savedOrder.getStatus())
                .totalAmount(savedOrder.getTotalAmount())
                .createdAt(savedOrder.getCreatedAt())
                .updatedAt(savedOrder.getUpdatedAt())
                .build();
    }
}
