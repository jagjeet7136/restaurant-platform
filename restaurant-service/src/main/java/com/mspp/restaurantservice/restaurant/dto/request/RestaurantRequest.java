package com.mspp.restaurantservice.restaurant.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalTime;

@Data
public class RestaurantRequest {

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String cuisine;

    @NotBlank
    private String phone;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private LocalTime openingTime;

    @NotNull
    private LocalTime closingTime;

    private Boolean active;
}