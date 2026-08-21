package com.mspp.restaurantservice.restaurant.specification;

import com.mspp.restaurantservice.restaurant.entity.Restaurant;
import org.springframework.data.jpa.domain.Specification;

public class RestaurantSpecification {

    public static Specification<Restaurant> hasCuisine(String cuisine) {
        return (root, query, criteriaBuilder) -> {
            if (cuisine == null || cuisine.isBlank()) {
                return null;
            }
            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("cuisine")),
                    cuisine.trim().toLowerCase()
            );
        };
    }

    public static Specification<Restaurant> hasActive(Boolean active) {
        return (root, query, criteriaBuilder) -> {
            if (active == null) {
                return null;
            }
            return criteriaBuilder.equal(
                    root.get("active"),
                    active
            );
        };
    }
}