package com.felipe.ecommerce_cart_service.infrastructure.mappers;

import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.core.domain.CartItem;
import com.felipe.ecommerce_cart_service.infrastructure.persistence.entities.CartEntity;
import com.felipe.ecommerce_cart_service.infrastructure.persistence.entities.CartItemEntity;
import org.springframework.stereotype.Component;

@Component
public class CartItemEntityMapper {
  public CartItem toDomain(CartItemEntity entity) {
    return CartItem.builder()
      .id(entity.getId())
      .productId(entity.getProductId())
      .productName(entity.getProductName())
      .thumbnailImage(entity.getThumbnailImage())
      .unitPrice(entity.getUnitPrice())
      .discountType(entity.getDiscountType())
      .discountValue(entity.getDiscountValue())
      .finalPrice(entity.getFinalPrice())
      .quantity(entity.getQuantity())
      .addedAt(entity.getAddedAt())
      .cart(toCartDomain(entity.getCart()))
      .build();
  }

  public CartItemEntity toEntity(CartItem cartItem) {
    return CartItemEntity.builder()
      .id(cartItem.getId())
      .productId(cartItem.getProductId())
      .productName(cartItem.getProductName())
      .thumbnailImage(cartItem.getThumbnailImage())
      .unitPrice(cartItem.getUnitPrice())
      .discountType(cartItem.getDiscountType())
      .discountValue(cartItem.getDiscountValue())
      .finalPrice(cartItem.getFinalPrice())
      .quantity(cartItem.getQuantity())
      .addedAt(cartItem.getAddedAt())
      .cart(toCartEntity(cartItem.getCart()))
      .build();
  }

  private Cart toCartDomain(CartEntity entity) {
    Cart cart = new Cart();
    cart.setId(entity.getId());
    cart.setCustomerId(entity.getCustomerId());
    cart.setCreatedAt(entity.getCreatedAt());
    return cart;
  }

  private CartEntity toCartEntity(Cart cart) {
    CartEntity cartEntity = new CartEntity();
    cartEntity.setId(cart.getId());
    cartEntity.setCustomerId(cart.getCustomerId());
    cartEntity.setCreatedAt(cart.getCreatedAt());
    return cartEntity;
  }
}
