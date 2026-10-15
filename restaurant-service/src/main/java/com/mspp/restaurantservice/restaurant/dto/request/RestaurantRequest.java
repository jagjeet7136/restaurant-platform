package com.mspp.restaurantservice.restaurant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalTime;

@Data
@Schema(description = "Request payload used to create or update a restaurant")
public class RestaurantRequest {

    @Schema(
            description = "Name of the restaurant",
            example = "Spice Garden"
    )
    @NotBlank(message = "Restaurant name is required")
    private String name;

    @Schema(
            description = "Short description of the restaurant",
            example = "Authentic Indian cuisine in downtown Toronto"
    )
    private String description;

    @Schema(
            description = "Cuisine served by the restaurant",
            example = "Indian"
    )
    @NotBlank(message = "Cuisine is required")
    private String cuisine;

    @Schema(
            description = "Restaurant contact phone number",
            example = "+1 416-555-1234"
    )
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[+]?[0-9\\-() ]{7,20}$",
            message = "Invalid phone number format"
    )
    private String phone;

    @Schema(
            description = "Restaurant contact email address",
            example = "contact@spicegarden.ca"
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(
            description = "Restaurant opening time",
            example = "11:00:00"
    )
    @NotNull(message = "Opening time is required")
    private LocalTime openingTime;

    @Schema(
            description = "Restaurant closing time",
            example = "22:00:00"
    )
    @NotNull(message = "Closing time is required")
    private LocalTime closingTime;

    @Schema(
            description = "Whether the restaurant is currently active",
            example = "true"
    )
    @NotNull(message = "Active status is required")
    private Boolean active;
}