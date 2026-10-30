package com.mspp.restaurantservice.restaurant.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
@Schema(description = "Restaurant information returned by the API")
public class RestaurantResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Spice Garden")
    private String name;

    @Schema(example = "Authentic Indian cuisine in downtown Toronto")
    private String description;

    @Schema(example = "Indian")
    private String cuisine;

    @Schema(example = "+1 416-555-1234")
    private String phone;

    @Schema(example = "contact@spicegarden.ca")
    private String email;

    @Schema(example = "11:00:00")
    private LocalTime openingTime;

    @Schema(example = "22:00:00")
    private LocalTime closingTime;

    @Schema(example = "true")
    private Boolean active;

    @Schema(
            description = "Date and time when the restaurant was created",
            example = "2026-10-15T18:30:00"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Date and time when the restaurant was last updated",
            example = "2026-10-15T19:15:00"
    )
    private LocalDateTime updatedAt;
}