package com.mspp.restaurantservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantResponse {

    private Long id;

    private String name;

    private String cuisine;

    private String address;

    private Double rating;

    private Boolean isOpen;
}