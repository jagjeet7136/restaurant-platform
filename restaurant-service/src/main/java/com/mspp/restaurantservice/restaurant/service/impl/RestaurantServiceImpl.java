package com.mspp.restaurantservice.restaurant.service.impl;

import com.mspp.restaurantservice.exception.BusinessValidationException;
import com.mspp.restaurantservice.exception.DuplicateResourceException;
import com.mspp.restaurantservice.exception.ResourceNotFoundException;
import com.mspp.restaurantservice.restaurant.dto.request.RestaurantRequest;
import com.mspp.restaurantservice.restaurant.dto.response.RestaurantResponse;
import com.mspp.restaurantservice.restaurant.entity.Restaurant;
import com.mspp.restaurantservice.restaurant.repository.RestaurantRepository;
import com.mspp.restaurantservice.restaurant.service.RestaurantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public RestaurantResponse createRestaurant(RestaurantRequest request) {
        validateOperatingHours(request);
        if (restaurantRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Restaurant with email '" + request.getEmail()
                            + "' already exists");
        }
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setCuisine(request.getCuisine());
        restaurant.setPhone(request.getPhone());
        restaurant.setEmail(request.getEmail());
        restaurant.setOpeningTime(request.getOpeningTime());
        restaurant.setClosingTime(request.getClosingTime());
        restaurant.setActive(request.getActive());

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return mapToResponse(savedRestaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurantById(Long id) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Restaurant not found with id: " + id));
        return mapToResponse(restaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantResponse> getAllRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return restaurantRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public RestaurantResponse updateRestaurant(Long id, RestaurantRequest request) {
        validateOperatingHours(request);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Restaurant not found with id: " + id));

        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setCuisine(request.getCuisine());
        restaurant.setPhone(request.getPhone());
        restaurant.setEmail(request.getEmail());
        restaurant.setOpeningTime(request.getOpeningTime());
        restaurant.setClosingTime(request.getClosingTime());
        restaurant.setActive(request.getActive());

        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
        return mapToResponse(updatedRestaurant);
    }

    @Override
    public void deleteRestaurant(Long id) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Restaurant not found with id: " + id));
        restaurantRepository.delete(restaurant);
    }

    private RestaurantResponse mapToResponse(Restaurant restaurant) {

        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .cuisine(restaurant.getCuisine())
                .phone(restaurant.getPhone())
                .email(restaurant.getEmail())
                .openingTime(restaurant.getOpeningTime())
                .closingTime(restaurant.getClosingTime())
                .active(restaurant.getActive())
                .createdAt(restaurant.getCreatedAt())
                .updatedAt(restaurant.getUpdatedAt())
                .build();
    }

    private void validateOperatingHours(RestaurantRequest request) {

        if (!request.getOpeningTime()
                .isBefore(request.getClosingTime())) {

            throw new BusinessValidationException(
                    "Opening time must be before closing time");
        }
    }
}