package com.mspp.restaurantservice.controller;

import com.mspp.restaurantservice.dto.request.RestaurantRequest;
import com.mspp.restaurantservice.dto.response.RestaurantResponse;
import com.mspp.restaurantservice.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponse createRestaurant(@Valid @RequestBody RestaurantRequest request){
        return restaurantService.createRestaurant(request);
    }
}