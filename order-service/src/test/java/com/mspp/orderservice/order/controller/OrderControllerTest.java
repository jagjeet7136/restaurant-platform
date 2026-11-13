package com.mspp.orderservice.order.controller;

import com.mspp.orderservice.exception.BusinessValidationException;
import com.mspp.orderservice.exception.OrderNotFoundException;
import com.mspp.orderservice.order.dto.request.OrderSearchRequest;
import com.mspp.orderservice.order.dto.response.OrderResponse;
import com.mspp.orderservice.order.dto.response.PageResponse;
import com.mspp.orderservice.order.entity.OrderStatus;
import com.mspp.orderservice.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Test
    void shouldCreateOrder() throws Exception {

        OrderResponse response = OrderResponse.builder()
                .id(1L)
                .restaurantId(2L)
                .status(OrderStatus.PENDING)
                .totalAmount(new BigDecimal("42.50"))
                .build();

        when(orderService.createOrder(any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "restaurantId": 2,
                                  "totalAmount": 42.50
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.restaurantId").value(2))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.totalAmount").value(42.50));

        verify(orderService).createOrder(any());
    }

    @Test
    void shouldReturn400WhenCreateRequestIsInvalid() throws Exception {

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "restaurantId": null,
                                  "totalAmount": 0
                                }
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(orderService);
    }

    @Test
    void shouldGetOrderById() throws Exception {

        OrderResponse response = OrderResponse.builder()
                .id(1L)
                .restaurantId(2L)
                .status(OrderStatus.PENDING)
                .totalAmount(new BigDecimal("42.50"))
                .build();

        when(orderService.getOrderById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(orderService).getOrderById(1L);
    }

    @Test
    void shouldReturn404WhenOrderDoesNotExist() throws Exception {

        when(orderService.getOrderById(999L))
                .thenThrow(new OrderNotFoundException(999L));

        mockMvc.perform(get("/api/v1/orders/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Order with ID 999 not found"));

        verify(orderService).getOrderById(999L);
    }

    @Test
    void shouldUpdateOrderStatus() throws Exception {

        OrderResponse response = OrderResponse.builder()
                .id(1L)
                .restaurantId(2L)
                .status(OrderStatus.CONFIRMED)
                .totalAmount(new BigDecimal("42.50"))
                .build();

        when(orderService.updateOrderStatus(
                1L,
                OrderStatus.CONFIRMED
        )).thenReturn(response);

        mockMvc.perform(patch("/api/v1/orders/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": "CONFIRMED"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(orderService)
                .updateOrderStatus(1L, OrderStatus.CONFIRMED);
    }

    @Test
    void shouldReturn400ForInvalidStatusTransition() throws Exception {

        when(orderService.updateOrderStatus(
                1L,
                OrderStatus.READY
        )).thenThrow(
                new BusinessValidationException(
                        "Order status cannot be changed from PENDING to READY"
                )
        );

        mockMvc.perform(patch("/api/v1/orders/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": "READY"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Order status cannot be changed from PENDING to READY"));
    }

    @Test
    void shouldReturn400WhenStatusIsMissing() throws Exception {

        mockMvc.perform(patch("/api/v1/orders/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                }
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(orderService);
    }

    @Test
    void shouldGetOrdersWithFilters() throws Exception {

        PageResponse<OrderResponse> response =
                PageResponse.<OrderResponse>builder()
                        .content(List.of())
                        .page(0)
                        .size(5)
                        .totalElements(0)
                        .totalPages(0)
                        .first(true)
                        .last(true)
                        .hasNext(false)
                        .hasPrevious(false)
                        .build();

        when(orderService.getAllOrders(
                any(OrderSearchRequest.class),
                eq(0),
                eq(5)
        )).thenReturn(response);

        mockMvc.perform(
                        get("/api/v1/orders")
                                .param("restaurantId", "2")
                                .param("status", "PENDING")
                                .param("page", "0")
                                .param("size", "5")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(5));

        ArgumentCaptor<OrderSearchRequest> captor =
                ArgumentCaptor.forClass(
                        OrderSearchRequest.class
                );

        verify(orderService).getAllOrders(
                captor.capture(),
                eq(0),
                eq(5)
        );

        OrderSearchRequest capturedRequest =
                captor.getValue();

        assertEquals(
                2L,
                capturedRequest.getRestaurantId()
        );

        assertEquals(
                OrderStatus.PENDING,
                capturedRequest.getStatus()
        );
    }

    @Test
    void shouldGetOrdersFilteredByStatus() throws Exception {

        when(orderService.getAllOrders(
                any(OrderSearchRequest.class),
                eq(0),
                eq(10)
        )).thenReturn(
                PageResponse.<OrderResponse>builder()
                        .content(List.of())
                        .page(0)
                        .size(10)
                        .totalElements(0)
                        .totalPages(0)
                        .first(true)
                        .last(true)
                        .hasNext(false)
                        .hasPrevious(false)
                        .build()
        );

        mockMvc.perform(
                        get("/api/v1/orders")
                                .param("status", "CONFIRMED")
                )
                .andExpect(status().isOk());

        ArgumentCaptor<OrderSearchRequest> captor =
                ArgumentCaptor.forClass(
                        OrderSearchRequest.class
                );

        verify(orderService).getAllOrders(
                captor.capture(),
                eq(0),
                eq(10)
        );

        assertNull(
                captor.getValue().getRestaurantId()
        );

        assertEquals(
                OrderStatus.CONFIRMED,
                captor.getValue().getStatus()
        );
    }

    @Test
    void shouldReturn400WhenStatusIsInvalid() throws Exception {

        mockMvc.perform(
                        get("/api/v1/orders")
                                .param("status", "HELLO")
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(orderService);
    }
}