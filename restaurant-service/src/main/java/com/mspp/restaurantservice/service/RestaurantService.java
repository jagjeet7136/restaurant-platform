package com.mspp.restaurantservice.service;

import com.mspp.restaurantservice.dto.request.RestaurantRequest;
import com.mspp.restaurantservice.dto.response.RestaurantResponse;

public interface RestaurantService {

    RestaurantResponse createRestaurant(RestaurantRequest request);

}