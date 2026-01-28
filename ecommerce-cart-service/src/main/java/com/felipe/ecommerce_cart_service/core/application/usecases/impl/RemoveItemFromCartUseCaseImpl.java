package com.felipe.ecommerce_cart_service.core.application.usecases.impl;

import com.felipe.ecommerce_cart_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_cart_service.core.application.exceptions.CartItemNotFoundException;
import com.felipe.ecommerce_cart_service.core.application.exceptions.CartNotFoundException;
import com.felipe.ecommerce_cart_service.core.application.gateway.CartGateway;
import com.felipe.ecommerce_cart_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_cart_service.core.application.usecases.RemoveItemFromCartUseCase;
import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.core.domain.CartItem;

import java.util.UUID;

public class RemoveItemFromCartUseCaseImpl implements RemoveItemFromCartUseCase {
  private final CartGateway cartGateway;
  private final CustomerGateway customerGateway;

  public RemoveItemFromCartUseCaseImpl(CartGateway cartGateway, CustomerGateway customerGateway) {
    this.cartGateway = cartGateway;
    this.customerGateway = customerGateway;
  }

  @Override
  public CartItem execute(Long itemId, String customerEmail) {
    CustomerProfileDTO customerProfile = this.customerGateway.fetchAuthCustomerProfile(customerEmail);
    UUID customerId = UUID.fromString(customerProfile.id());
    Cart foundCart = this.cartGateway.findCartByCustomerId(customerId)
      .orElseThrow(() -> new CartNotFoundException("Carrinho do cliente de id '" + customerId + "' não encontrado"));

    CartItem cartItem = foundCart.getItems()
      .stream()
      .filter(item -> item.getId().equals(itemId))
      .findFirst()
      .orElseThrow(() -> new CartItemNotFoundException(itemId));

    foundCart.removeItem(cartItem);
    this.cartGateway.removeItemFromCart(foundCart);
    return cartItem;
  }
}
