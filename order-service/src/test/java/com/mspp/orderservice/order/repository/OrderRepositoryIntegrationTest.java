package com.mspp.orderservice.order.repository;

import com.mspp.orderservice.order.dto.request.OrderSearchRequest;
import com.mspp.orderservice.order.entity.Order;
import com.mspp.orderservice.order.entity.OrderStatus;
import com.mspp.orderservice.order.specification.OrderSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
@Rollback
class OrderRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.4");

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {

        createOrder(
                2L,
                OrderStatus.PENDING,
                "20.00"
        );

        createOrder(
                2L,
                OrderStatus.CONFIRMED,
                "30.00"
        );

        createOrder(
                5L,
                OrderStatus.PENDING,
                "40.00"
        );

        createOrder(
                5L,
                OrderStatus.CANCELLED,
                "50.00"
        );
    }

    @Test
    void shouldFilterOrdersByStatus() {

        OrderSearchRequest request =
                new OrderSearchRequest();

        request.setStatus(OrderStatus.PENDING);

        Page<Order> result =
                orderRepository.findAll(
                        OrderSpecification.build(request),
                        PageRequest.of(0, 10)
                );

        assertEquals(2, result.getTotalElements());

        assertTrue(
                result.getContent()
                        .stream()
                        .allMatch(order ->
                                order.getStatus()
                                        == OrderStatus.PENDING)
        );
    }

    @Test
    void shouldFilterOrdersByRestaurantId() {

        OrderSearchRequest request =
                new OrderSearchRequest();

        request.setRestaurantId(2L);

        Page<Order> result =
                orderRepository.findAll(
                        OrderSpecification.build(request),
                        PageRequest.of(0, 10)
                );

        assertEquals(2, result.getTotalElements());

        assertTrue(
                result.getContent()
                        .stream()
                        .allMatch(order ->
                                order.getRestaurantId().equals(2L))
        );
    }

    @Test
    void shouldFilterOrdersByRestaurantIdAndStatus() {

        OrderSearchRequest request =
                new OrderSearchRequest();

        request.setRestaurantId(2L);
        request.setStatus(OrderStatus.PENDING);

        Page<Order> result =
                orderRepository.findAll(
                        OrderSpecification.build(request),
                        PageRequest.of(0, 10)
                );

        assertEquals(1, result.getTotalElements());

        Order order = result.getContent().get(0);

        assertEquals(2L, order.getRestaurantId());
        assertEquals(
                OrderStatus.PENDING,
                order.getStatus()
        );
    }

    @Test
    void shouldReturnAllOrdersWhenNoFiltersProvided() {

        OrderSearchRequest request =
                new OrderSearchRequest();

        Page<Order> result =
                orderRepository.findAll(
                        OrderSpecification.build(request),
                        PageRequest.of(0, 10)
                );

        assertEquals(4, result.getTotalElements());
    }

    private Order createOrder(
            Long restaurantId,
            OrderStatus status,
            String totalAmount
    ) {

        Order order = new Order();

        order.setRestaurantId(restaurantId);
        order.setStatus(status);
        order.setTotalAmount(
                new BigDecimal(totalAmount)
        );

        return orderRepository.save(order);
    }
}