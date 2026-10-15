package com.mspp.restaurantservice.restaurant.controller;

import com.mspp.restaurantservice.exception.ErrorResponse;
import com.mspp.restaurantservice.restaurant.dto.request.RestaurantRequest;
import com.mspp.restaurantservice.restaurant.dto.request.RestaurantSearchRequest;
import com.mspp.restaurantservice.restaurant.dto.response.PageResponse;
import com.mspp.restaurantservice.restaurant.dto.response.RestaurantResponse;
import com.mspp.restaurantservice.restaurant.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Restaurants",
        description = "APIs for restaurant management"
)
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a restaurant",
            description = "Creates a new restaurant"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Restaurant created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid restaurant data",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Restaurant with the same email already exists",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public RestaurantResponse createRestaurant(@Valid @RequestBody RestaurantRequest request){
        return restaurantService.createRestaurant(request);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get restaurant by ID",
            description = "Returns a restaurant using its unique ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Restaurant found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurant not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public RestaurantResponse getRestaurantById(@PathVariable Long id) {
        return restaurantService.getRestaurantById(id);
    }

    @GetMapping
    @Operation(
            summary = "Search restaurants",
            description = "Returns the list of restaurants based on provided filters"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful page of restaurants search"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Invalid values provided of filters",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public PageResponse<RestaurantResponse> getAllRestaurants(@RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page number must be 0 or greater") int page,
                                                              @RequestParam(defaultValue = "10")
                                                              @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 100, message = "Page size must not exceed 100") int size,
                                                              @RequestParam(defaultValue = "id") String sortBy,
                                                              @RequestParam(defaultValue = "asc") String direction,
                                                              @ModelAttribute @Valid RestaurantSearchRequest restaurantSearchRequest) {
        return restaurantService.getAllRestaurants(page, size, sortBy, direction, restaurantSearchRequest);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a restaurant",
            description = "Updates an existing restaurant using its unique ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Restaurant updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid restaurant data",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurant with Id not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public RestaurantResponse updateRestaurant(@PathVariable Long id, @Valid @RequestBody RestaurantRequest request) {
        return restaurantService.updateRestaurant(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete a restaurant",
            description = "Deletes a restaurant using its unique ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Restaurant deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurant not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public void deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
    }
}