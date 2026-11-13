package com.mspp.orderservice.order.specification;

import com.mspp.orderservice.order.dto.request.OrderSearchRequest;
import com.mspp.orderservice.order.entity.Order;
import org.springframework.data.jpa.domain.Specification;

public final class OrderSpecification {

    private OrderSpecification() {}

    public static Specification<Order> build(OrderSearchRequest request) {

        Specification<Order> specification = Specification.unrestricted();

        if (request.getRestaurantId() != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("restaurantId"),
                                    request.getRestaurantId()
                            )
            );
        }

        if (request.getStatus() != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("status"),
                                    request.getStatus()
                            )
            );
        }

        return specification;
    }
}