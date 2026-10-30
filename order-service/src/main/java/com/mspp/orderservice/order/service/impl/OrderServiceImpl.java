package com.mspp.orderservice.order.service.impl;

import com.mspp.orderservice.client.restaurant.RestaurantClientResponse;
import com.mspp.orderservice.client.restaurant.RestaurantServiceGateway;
import com.mspp.orderservice.exception.BusinessValidationException;
import com.mspp.orderservice.exception.OrderNotFoundException;
import com.mspp.orderservice.order.dto.request.OrderCreateRequest;
import com.mspp.orderservice.order.dto.response.OrderResponse;
import com.mspp.orderservice.order.entity.Order;
import com.mspp.orderservice.order.entity.OrderStatus;
import com.mspp.orderservice.order.repository.OrderRepository;
import com.mspp.orderservice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantServiceGateway restaurantServiceGateway;

    @Override
    public OrderResponse createOrder(OrderCreateRequest request) {

        RestaurantClientResponse restaurant =
                restaurantServiceGateway.getRestaurantById(
                        request.getRestaurantId()
                );

        if (!Boolean.TRUE.equals(restaurant.getActive())) {
            throw new BusinessValidationException(
                    "Cannot place an order for an inactive restaurant"
            );
        }

        Order order = new Order();
        order.setRestaurantId(restaurant.getId());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(request.getTotalAmount());

        Order savedOrder = orderRepository.save(order);

        return toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        return toResponse(order);
    }

    @Override
    public OrderResponse updateOrderStatus(Long id, OrderStatus newStatus) {

        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        validateStatusTransition(order.getStatus(), newStatus);
        order.setStatus(newStatus);
        return toResponse(order);
    }

    private OrderResponse toResponse(Order order) {

        return OrderResponse.builder()
                .id(order.getId())
                .restaurantId(order.getRestaurantId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {

        boolean validTransition = switch (currentStatus) {
            case PENDING ->
                    newStatus == OrderStatus.CONFIRMED
                            || newStatus == OrderStatus.CANCELLED;
            case CONFIRMED ->
                    newStatus == OrderStatus.PREPARING
                            || newStatus == OrderStatus.CANCELLED;
            case PREPARING ->
                    newStatus == OrderStatus.READY
                            || newStatus == OrderStatus.CANCELLED;
            case READY ->
                    newStatus == OrderStatus.COMPLETED;
            case COMPLETED, CANCELLED -> false;
        };

        if (!validTransition) {
            throw new BusinessValidationException("Order status cannot be changed from " + currentStatus + " to " + newStatus);
        }
    }
}
