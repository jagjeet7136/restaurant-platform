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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestaurantServiceGateway restaurantServiceGateway;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void shouldCreateOrderWhenRestaurantIsActive() {

        OrderCreateRequest request = new OrderCreateRequest();
        request.setRestaurantId(2L);
        request.setTotalAmount(new BigDecimal("42.50"));

        RestaurantClientResponse restaurant = new RestaurantClientResponse();
        restaurant.setId(2L);
        restaurant.setName("Spice Garden");
        restaurant.setActive(true);

        when(restaurantServiceGateway.getRestaurantById(2L))
                .thenReturn(restaurant);

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order order = invocation.getArgument(0);
                    order.setId(1L);
                    return order;
                });

        OrderResponse response = orderService.createOrder(request);

        assertEquals(1L, response.getId());
        assertEquals(2L, response.getRestaurantId());
        assertEquals(OrderStatus.PENDING, response.getStatus());
        assertEquals(new BigDecimal("42.50"), response.getTotalAmount());

        verify(restaurantServiceGateway).getRestaurantById(2L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldRejectOrderWhenRestaurantIsInactive() {

        OrderCreateRequest request = new OrderCreateRequest();
        request.setRestaurantId(2L);
        request.setTotalAmount(new BigDecimal("42.50"));

        RestaurantClientResponse restaurant = new RestaurantClientResponse();
        restaurant.setId(2L);
        restaurant.setActive(false);

        when(restaurantServiceGateway.getRestaurantById(2L))
                .thenReturn(restaurant);

        assertThrows(
                BusinessValidationException.class,
                () -> orderService.createOrder(request)
        );

        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenOrderDoesNotExist() {

        when(orderRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                OrderNotFoundException.class,
                () -> orderService.getOrderById(999L)
        );
    }

    @Test
    void shouldChangeStatusFromPendingToConfirmed() {

        Order order = createOrder(1L, OrderStatus.PENDING);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        OrderResponse response = orderService.updateOrderStatus(1L, OrderStatus.CONFIRMED);

        assertEquals(OrderStatus.CONFIRMED, response.getStatus());

        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
    }

    private Order createOrder(Long id, OrderStatus status) {

        Order order = new Order();
        order.setId(id);
        order.setRestaurantId(2L);
        order.setStatus(status);
        order.setTotalAmount(new BigDecimal("42.50"));

        return order;
    }
}
