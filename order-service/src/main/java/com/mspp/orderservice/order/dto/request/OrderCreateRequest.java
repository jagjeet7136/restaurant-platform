package com.mspp.orderservice.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "Request used to create an order")
@Data
public class OrderCreateRequest {

    @Schema(
            description = "Restaurant accepting the order",
            example = "2"
    )
    @NotNull(message = "Restaurant ID is required")
    @Positive(message = "Restaurant ID must be positive")
    private Long restaurantId;

    @Schema(
            description = "Current order total",
            example = "42.50"
    )
    @NotNull(message = "Total amount is required")
    @DecimalMin(
            value = "0.01",
            message = "Total amount must be greater than zero"
    )
    @Digits(
            integer = 10,
            fraction = 2,
            message = "Total amount must contain at most 10 integer digits and 2 decimal places"
    )
    private BigDecimal totalAmount;
}