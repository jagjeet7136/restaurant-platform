package com.mspp.orderservice.order.dto.response;

import com.mspp.orderservice.order.entity.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Order response")
@Getter
@Builder
public class OrderResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "2")
    private Long restaurantId;

    @Schema(example = "PENDING")
    private OrderStatus status;

    @Schema(example = "42.50")
    private BigDecimal totalAmount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}