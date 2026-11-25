package com.mspp.orderservice.order.dto.request;

import com.mspp.orderservice.order.entity.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Optional criteria used to search orders")
public class OrderSearchRequest {

    @Schema(
            description = "Restaurant ID to filter orders by",
            example = "2"
    )
    private Long restaurantId;

    @Schema(
            description = "Order status to filter by",
            example = "PENDING"
    )
    private OrderStatus status;
}