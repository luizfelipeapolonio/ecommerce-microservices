package com.felipe.ecommerce_cart_service.infrastructure.mappers;

import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.core.domain.CartItem;
import com.felipe.ecommerce_cart_service.infrastructure.persistence.entities.CartEntity;
import com.felipe.ecommerce_cart_service.infrastructure.persistence.entities.CartItemEntity;
import org.springframework.stereotype.Component;

@Component
public class CartEntityMapper {
  public Cart toDomain(CartEntity entity) {
    Cart cart = new Cart();
    cart.setId(entity.getId());
    cart.setCustomerId(entity.getCustomerId());
    cart.setCreatedAt(entity.getCreatedAt());

    entity.getItems().forEach(item -> {
      CartItem cartItem = toCartItemDomain(item);
      cart.addItem(cartItem);
    });

    return cart;
  }

  public CartEntity toEntity(Cart cart) {
    CartEntity entity = new CartEntity();
    entity.setId(cart.getId());
    entity.setCustomerId(cart.getCustomerId());
    entity.setCreatedAt(cart.getCreatedAt());

    cart.getItems().forEach(item -> {
      CartItemEntity cartItemEntity = toCartItemEntity(item);
      entity.addCartItem(cartItemEntity);
    });

    return entity;
  }

  private CartItem toCartItemDomain(CartItemEntity entity) {
    CartItem item = new CartItem();
    item.setId(entity.getId());
    item.setProductId(entity.getProductId());
    item.setQuantity(entity.getQuantity());
    item.setAddedAt(entity.getAddedAt());
    return item;
  }

  private CartItemEntity toCartItemEntity(CartItem item) {
    CartItemEntity entity = new CartItemEntity();
    entity.setId(item.getId());
    entity.setProductId(item.getProductId());
    entity.setQuantity(item.getQuantity());
    entity.setAddedAt(item.getAddedAt());
    return entity;
  }
}
