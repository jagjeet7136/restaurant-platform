package com.mspp.restaurantservice.restaurant.dto.request;

import lombok.Data;

@Data
public class RestaurantSearchRequest {

    private String cuisine;
    private Boolean active;
    private String name;
}