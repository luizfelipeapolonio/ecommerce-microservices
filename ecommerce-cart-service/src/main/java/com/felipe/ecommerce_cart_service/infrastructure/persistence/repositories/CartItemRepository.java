package com.felipe.ecommerce_cart_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_cart_service.infrastructure.persistence.entities.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
  Optional<CartItemEntity> findByProductIdAndCartId(UUID productId, UUID  cartId);
}
