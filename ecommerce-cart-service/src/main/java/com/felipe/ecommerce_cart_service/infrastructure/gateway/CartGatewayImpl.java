package com.felipe.ecommerce_cart_service.infrastructure.gateway;

import com.felipe.ecommerce_cart_service.core.application.gateway.CartGateway;
import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.infrastructure.mappers.CartEntityMapper;
import com.felipe.ecommerce_cart_service.infrastructure.persistence.entities.CartEntity;
import com.felipe.ecommerce_cart_service.infrastructure.persistence.repositories.CartRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CartGatewayImpl implements CartGateway {
  private final CartRepository cartRepository;
  private final CartEntityMapper cartEntityMapper;

  public CartGatewayImpl(CartRepository cartRepository, CartEntityMapper cartEntityMapper) {
    this.cartRepository = cartRepository;
    this.cartEntityMapper = cartEntityMapper;
  }

  @Override
  public Cart createCart(UUID customerId) {
    final CartEntity entity = new CartEntity(customerId);
    return this.cartEntityMapper.toDomain(this.cartRepository.save(entity));
  }
}
