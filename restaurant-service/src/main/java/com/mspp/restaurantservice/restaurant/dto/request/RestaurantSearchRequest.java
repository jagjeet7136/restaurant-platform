package com.mspp.restaurantservice.restaurant.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RestaurantSearchRequest {

    @Size(max = 50, message = "Cuisine must not exceed 50 characters")
    private String cuisine;

    private Boolean active;

    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;
}