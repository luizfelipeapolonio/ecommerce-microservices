package com.felipe.ecommerce_order_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OrderSagaRepository extends JpaRepository<OrderSaga, UUID> {
  Optional<OrderSaga> findByOrderId(UUID orderId);

  @Query("SELECT o FROM OrderSaga o JOIN FETCH o.participants WHERE o.orderId = :orderId")
  Optional<OrderSaga> findByOrderIdWithParticipants(@Param("orderId") UUID orderId);
}
