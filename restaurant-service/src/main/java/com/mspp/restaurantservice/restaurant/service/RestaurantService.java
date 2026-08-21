package com.mspp.restaurantservice.restaurant.service;

import com.mspp.restaurantservice.restaurant.dto.request.RestaurantRequest;
import com.mspp.restaurantservice.restaurant.dto.response.RestaurantResponse;
import org.springframework.data.domain.Page;

public interface RestaurantService {

    RestaurantResponse createRestaurant(RestaurantRequest request);
    RestaurantResponse getRestaurantById(Long id);
    Page<RestaurantResponse> getAllRestaurants(int page, int size, String sortBy, String direction, String cuisine,
                                               Boolean active);
    RestaurantResponse updateRestaurant(Long id, RestaurantRequest request);
    void deleteRestaurant(Long id);
}