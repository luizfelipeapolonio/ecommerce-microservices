package com.felipe.ecommerce_order_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
}
