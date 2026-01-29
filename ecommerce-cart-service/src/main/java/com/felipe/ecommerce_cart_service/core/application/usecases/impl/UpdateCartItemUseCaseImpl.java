package com.felipe.ecommerce_cart_service.core.application.usecases.impl;

import com.felipe.ecommerce_cart_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_cart_service.core.application.exceptions.CartItemNotFoundException;
import com.felipe.ecommerce_cart_service.core.application.exceptions.CartNotFoundException;
import com.felipe.ecommerce_cart_service.core.application.gateway.CartGateway;
import com.felipe.ecommerce_cart_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_cart_service.core.application.usecases.UpdateCartItemUseCase;
import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.core.domain.CartItem;
import com.felipe.utils.product.PriceCalculator;

import java.math.BigDecimal;
import java.util.UUID;

public class UpdateCartItemUseCaseImpl implements UpdateCartItemUseCase {
  private final CartGateway cartGateway;
  private final CustomerGateway customerGateway;

  public UpdateCartItemUseCaseImpl(CartGateway cartGateway, CustomerGateway customerGateway) {
    this.cartGateway = cartGateway;
    this.customerGateway = customerGateway;
  }

  @Override
  public CartItem execute(Long itemId, int quantity, String customerEmail) {
    CustomerProfileDTO customerProfile = this.customerGateway.fetchAuthCustomerProfile(customerEmail);
    UUID customerId = UUID.fromString(customerProfile.id());
    Cart foundCart = this.cartGateway.findCartByCustomerId(customerId)
      .orElseThrow(() -> new CartNotFoundException("Carrinho do cliente de id '" + customerId + "' não encontrado"));

    CartItem updatedItem = foundCart.getItems()
      .stream()
      .filter(item -> item.getId().equals(itemId))
      .findFirst()
      .map(item -> {
        BigDecimal finalPrice = item.getDiscountType() == null ?
          PriceCalculator.calculateFinalPrice(item.getUnitPrice().toString(), quantity) :
          PriceCalculator.calculateFinalPrice(item.getDiscountType(), item.getUnitPrice().toString(), item.getDiscountValue(), quantity);
        item.setFinalPrice(finalPrice);
        item.setQuantity(quantity);
        return item;
      })
      .orElseThrow(() -> new CartItemNotFoundException(itemId));

    this.cartGateway.updateCartItem(foundCart);
    return updatedItem;
  }
}
