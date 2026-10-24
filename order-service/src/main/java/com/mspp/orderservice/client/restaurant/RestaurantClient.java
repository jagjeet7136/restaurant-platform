package com.mspp.orderservice.client.restaurant;

import com.mspp.orderservice.exception.ExternalServiceUnavailableException;
import com.mspp.orderservice.exception.RestaurantNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class RestaurantClient {

    private final RestClient restClient;

    public RestaurantClient(RestClient.Builder restClientBuilder, @Value("${services.restaurant.base-url}") String baseUrl) {
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    @Retryable(
            includes = ExternalServiceUnavailableException.class,
            maxRetries = 2,
            delay = 300,
            multiplier = 2,
            maxDelay = 1000
    )
    public RestaurantClientResponse getRestaurantById(Long restaurantId) {
        log.info("Calling Restaurant Service for restaurantId={}", restaurantId);
        try {
            return restClient
                    .get()
                    .uri("/api/v1/restaurants/{id}", restaurantId)
                    .retrieve()
                    .body(RestaurantClientResponse.class);

        } catch (HttpClientErrorException.NotFound ex) {
            throw new RestaurantNotFoundException(restaurantId);

        } catch (ResourceAccessException | HttpServerErrorException ex) {
            throw new ExternalServiceUnavailableException(
                    "Restaurant Service is currently unavailable"
            );
        }
    }
}