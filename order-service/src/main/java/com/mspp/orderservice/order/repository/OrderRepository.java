package com.mspp.orderservice.order.repository;

import com.mspp.orderservice.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}