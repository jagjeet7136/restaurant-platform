package com.mspp.restaurantservice.restaurant.service;

import com.mspp.restaurantservice.restaurant.dto.request.RestaurantRequest;
import com.mspp.restaurantservice.restaurant.dto.request.RestaurantSearchRequest;
import com.mspp.restaurantservice.restaurant.dto.response.PageResponse;
import com.mspp.restaurantservice.restaurant.dto.response.RestaurantResponse;

public interface RestaurantService {

    RestaurantResponse createRestaurant(RestaurantRequest request);
    RestaurantResponse getRestaurantById(Long id);
    PageResponse<RestaurantResponse> getAllRestaurants(int page, int size, String sortBy, String direction,
                                                       RestaurantSearchRequest restaurantSearchRequest);
    RestaurantResponse updateRestaurant(Long id, RestaurantRequest request);
    void deleteRestaurant(Long id);
}