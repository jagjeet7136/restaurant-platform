package com.mspp.restaurantservice.restaurant.repository;

import com.mspp.restaurantservice.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository
        extends JpaRepository<Restaurant, Long> {

}