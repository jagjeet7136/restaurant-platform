package com.mspp.orderservice.client.restaurant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestaurantClient {

    private final RestClient restClient;

    public RestaurantClient(
            RestClient.Builder restClientBuilder,
            @Value("${services.restaurant.base-url}") String baseUrl
    ) {
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public RestaurantClientResponse getRestaurantById(Long restaurantId) {

        return restClient
                .get()
                .uri("/api/v1/restaurants/{id}", restaurantId)
                .retrieve()
                .body(RestaurantClientResponse.class);
    }
}