package com.mspp.orderservice.order.dto.request;

import com.mspp.orderservice.order.entity.OrderStatus;
import lombok.Data;

@Data
public class OrderSearchRequest {

    private Long restaurantId;

    private OrderStatus status;
}