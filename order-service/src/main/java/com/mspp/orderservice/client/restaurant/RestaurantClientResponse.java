package com.mspp.orderservice.client.restaurant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantClientResponse {

    private Long id;
    private String name;
    private Boolean active;
}