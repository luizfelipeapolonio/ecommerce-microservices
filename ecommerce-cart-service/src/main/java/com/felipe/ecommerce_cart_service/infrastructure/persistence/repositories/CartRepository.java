package com.felipe.ecommerce_cart_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_cart_service.infrastructure.persistence.entities.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<CartEntity, UUID> {
  Optional<CartEntity> findByCustomerId(UUID customerId);
}
