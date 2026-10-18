package com.mspp.orderservice.order.dto.response;

import com.mspp.orderservice.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class OrderResponse {

    private Long id;

    private Long restaurantId;

    private OrderStatus status;

    private BigDecimal totalAmount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}