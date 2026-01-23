package com.felipe.ecommerce_cart_service.infrastructure.dtos.cart;

import com.felipe.ecommerce_cart_service.core.domain.CartItem;

public record CartItemResponseDTO(
  Long id,
  String productId,
  String productName,
  String thumbnailImage,
  String unitPrice,
  String discountType,
  String discountValue,
  String finalPrice,
  int quantity,
  String cartId,
  String addedAt
) {
  public CartItemResponseDTO(CartItem cartItem) {
    this(
      cartItem.getId(),
      cartItem.getProductId().toString(),
      cartItem.getProductName(),
      cartItem.getThumbnailImage(),
      cartItem.getUnitPrice().toString(),
      cartItem.getDiscountType(),
      cartItem.getDiscountValue(),
      cartItem.getFinalPrice().toString(),
      cartItem.getQuantity(),
      cartItem.getCart().getId().toString(),
      cartItem.getAddedAt().toString()
    );
  }
}
