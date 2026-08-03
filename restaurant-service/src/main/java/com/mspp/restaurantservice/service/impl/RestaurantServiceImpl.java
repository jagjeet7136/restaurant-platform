package com.mspp.restaurantservice.service.impl;

import com.mspp.restaurantservice.dto.request.RestaurantRequest;
import com.mspp.restaurantservice.dto.response.RestaurantResponse;
import com.mspp.restaurantservice.entity.Restaurant;
import com.mspp.restaurantservice.repository.RestaurantRepository;
import com.mspp.restaurantservice.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {


    private final RestaurantRepository restaurantRepository;


    @Override
    public RestaurantResponse createRestaurant(RestaurantRequest request) {
        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .cuisine(request.getCuisine())
                .address(request.getAddress())
                .rating(request.getRating())
                .isOpen(request.getIsOpen())
                .build();

        Restaurant savedRestaurant =
                restaurantRepository.save(restaurant);

        return RestaurantResponse.builder()
                .id(savedRestaurant.getId())
                .name(savedRestaurant.getName())
                .cuisine(savedRestaurant.getCuisine())
                .address(savedRestaurant.getAddress())
                .rating(savedRestaurant.getRating())
                .isOpen(savedRestaurant.getIsOpen())
                .build();
    }
}