package com.mspp.restaurantservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestaurantRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String cuisine;

    @NotBlank
    private String address;

    private Double rating;

    private Boolean isOpen;
}