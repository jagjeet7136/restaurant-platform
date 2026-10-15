package com.mspp.restaurantservice.restaurant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Optional filters used when searching restaurants")
public class RestaurantSearchRequest {

    @Schema(
            description = "Filter restaurants by cuisine",
            example = "Indian"
    )
    @Size(
            max = 50,
            message = "Cuisine must not exceed 50 characters"
    )
    private String cuisine;

    @Schema(
            description = "Filter restaurants by active status",
            example = "true"
    )
    private Boolean active;

    @Schema(
            description = "Filter restaurants by partial restaurant name",
            example = "Spice"
    )
    @Size(
            max = 100,
            message = "Name must not exceed 100 characters"
    )
    private String name;
}