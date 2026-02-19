package com.felipe.ecommerce_order_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderSagaRepository extends JpaRepository<OrderSaga, UUID> {
}
