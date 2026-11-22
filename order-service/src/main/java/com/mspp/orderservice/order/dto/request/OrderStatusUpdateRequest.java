package com.mspp.orderservice.order.dto.request;

import com.mspp.orderservice.order.entity.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request used to change an order status")
public class OrderStatusUpdateRequest {

    @Schema(
            description = "Requested new status",
            example = "CONFIRMED"
    )
    @NotNull(message = "Order status is required")
    private OrderStatus status;
}