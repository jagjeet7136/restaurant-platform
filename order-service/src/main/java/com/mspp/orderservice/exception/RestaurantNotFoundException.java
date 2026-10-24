package com.mspp.orderservice.exception;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(Long restaurantId) {
        super("Restaurant with ID " + restaurantId + " not found");
    }
}