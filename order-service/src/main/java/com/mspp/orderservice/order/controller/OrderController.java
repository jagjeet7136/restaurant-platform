package com.mspp.orderservice.order.controller;

import com.mspp.orderservice.order.dto.request.OrderCreateRequest;
import com.mspp.orderservice.order.dto.request.OrderSearchRequest;
import com.mspp.orderservice.order.dto.request.OrderStatusUpdateRequest;
import com.mspp.orderservice.order.dto.response.OrderResponse;
import com.mspp.orderservice.order.dto.response.PageResponse;
import com.mspp.orderservice.order.entity.OrderStatus;
import com.mspp.orderservice.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(
        name = "Orders",
        description = "Order management APIs"
)
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create an order",
            description = "Creates a new order after validating that the restaurant exists and is active"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Order created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or restaurant is inactive"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurant not found"
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Restaurant Service is unavailable"
            )
    })
    public OrderResponse createOrder(@Valid @RequestBody OrderCreateRequest request) {
        return orderService.createOrder(request);
    }

    @Operation(
            summary = "Get order by ID",
            description = "Returns a single order using its unique identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Order found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Order not found"
            )
    })
    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @Operation(
            summary = "Update order status",
            description = "Changes an order status according to the allowed order lifecycle transitions"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Order status updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid status transition"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Order not found"
            )
    })
    @PatchMapping("/{id}/status")
    public OrderResponse updateOrderStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusUpdateRequest request) {
        return orderService.updateOrderStatus(id, request.getStatus());
    }

    @GetMapping
    @Operation(
            summary = "Search orders",
            description = "Returns paginated orders with optional restaurant and status filters"
    )
    public PageResponse<OrderResponse> getAllOrders(

            @Parameter(
                    description = "Filter orders by restaurant ID",
                    example = "2"
            )
            @RequestParam(required = false)
            Long restaurantId,

            @Parameter(
                    description = "Filter orders by status",
                    example = "PENDING"
            )
            @RequestParam(required = false)
            OrderStatus status,

            @Parameter(
                    description = "Zero-based page number",
                    example = "0"
            )
            @RequestParam(defaultValue = "0")
            int page,

            @Parameter(
                    description = "Number of orders per page",
                    example = "10"
            )
            @RequestParam(defaultValue = "10")
            int size
    ) {

        OrderSearchRequest searchRequest = new OrderSearchRequest();
        searchRequest.setRestaurantId(restaurantId);
        searchRequest.setStatus(status);
        return orderService.getAllOrders(searchRequest, page, size
        );
    }
}
