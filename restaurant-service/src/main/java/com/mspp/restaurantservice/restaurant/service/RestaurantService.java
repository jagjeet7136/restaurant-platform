package com.mspp.restaurantservice.restaurant.service;

import com.mspp.restaurantservice.restaurant.dto.request.RestaurantRequest;
import com.mspp.restaurantservice.restaurant.dto.response.RestaurantResponse;

import java.util.List;

public interface RestaurantService {

    RestaurantResponse createRestaurant(RestaurantRequest request);
    RestaurantResponse getRestaurantById(Long id);
    List<RestaurantResponse> getAllRestaurants();
    RestaurantResponse updateRestaurant(Long id, RestaurantRequest request);
    void deleteRestaurant(Long id);
}