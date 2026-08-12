package com.mspp.restaurantservice.restaurant.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class RestaurantResponse {

    private Long id;

    private String name;

    private String description;

    private String cuisine;

    private String phone;

    private String email;

    private LocalTime openingTime;

    private LocalTime closingTime;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}