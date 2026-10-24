package com.mspp.orderservice.client.restaurant;

import com.mspp.orderservice.exception.ExternalServiceUnavailableException;
import com.mspp.orderservice.exception.RestaurantNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantServiceGateway {

    private final RestaurantClient restaurantClient;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    public RestaurantClientResponse getRestaurantById(Long restaurantId) {

        return circuitBreakerFactory
                .create("restaurantService")
                .run(
                        () -> restaurantClient.getRestaurantById(restaurantId),
                        throwable -> handleFailure(throwable)
                );
    }

    private RestaurantClientResponse handleFailure(Throwable throwable) {

        if (throwable instanceof RestaurantNotFoundException ex) {
            throw ex;
        }

        if (throwable instanceof ExternalServiceUnavailableException ex) {
            throw ex;
        }

        throw new ExternalServiceUnavailableException(
                "Restaurant Service is currently unavailable"
        );
    }
}