package com.felipe.ecommerce_cart_service.core.application.dtos;

import com.felipe.ecommerce_cart_service.core.domain.Cart;

public record CartItemDTO(int itemIndex, Cart cart) {
}
