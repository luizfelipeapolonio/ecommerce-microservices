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
    return CartItem.builder()
      .id(entity.getId())
      .productId(entity.getProductId())
      .productName(entity.getProductName())
      .thumbnailImage(entity.getThumbnailImage())
      .unitPrice(entity.getUnitPrice())
      .discountType(entity.getDiscountType())
      .discountValue(entity.getDiscountValue())
      .quantity(entity.getQuantity())
      .addedAt(entity.getAddedAt())
      .build();
  }

  private CartItemEntity toCartItemEntity(CartItem item) {
    return CartItemEntity.builder()
      .id(item.getId())
      .productId(item.getProductId())
      .productName(item.getProductName())
      .thumbnailImage(item.getThumbnailImage())
      .unitPrice(item.getUnitPrice())
      .discountType(item.getDiscountType())
      .discountValue(item.getDiscountValue())
      .quantity(item.getQuantity())
      .addedAt(item.getAddedAt())
      .build();
  }
}
