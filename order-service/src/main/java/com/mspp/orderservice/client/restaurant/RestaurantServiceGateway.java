package com.mspp.orderservice.client.restaurant;

import com.mspp.orderservice.exception.ExternalServiceUnavailableException;
import com.mspp.orderservice.exception.RestaurantNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RestaurantServiceGateway {

    private final RestaurantClient restaurantClient;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    public RestaurantClientResponse getRestaurantById(Long restaurantId) {

        return circuitBreakerFactory
                .create("restaurantService")
                .run(
                        () -> restaurantClient.getRestaurantById(restaurantId),
                        this::handleFailure
                );
    }

    private RestaurantClientResponse handleFailure(Throwable throwable) {

        log.error(
                "Circuit breaker fallback. Type={}, message={}",
                throwable.getClass().getName(),
                throwable.getMessage()
        );

        RestaurantNotFoundException notFound =
                findCause(
                        throwable,
                        RestaurantNotFoundException.class
                );

        if (notFound != null) {
            throw notFound;
        }

        ExternalServiceUnavailableException unavailable =
                findCause(
                        throwable,
                        ExternalServiceUnavailableException.class
                );

        if (unavailable != null) {
            throw unavailable;
        }

        throw new ExternalServiceUnavailableException(
                "Restaurant Service is currently unavailable"
        );
    }

    private <T extends Throwable> T findCause(
            Throwable throwable,
            Class<T> exceptionType
    ) {

        Throwable current = throwable;

        while (current != null) {

            if (exceptionType.isInstance(current)) {
                return exceptionType.cast(current);
            }

            current = current.getCause();
        }

        return null;
    }
}