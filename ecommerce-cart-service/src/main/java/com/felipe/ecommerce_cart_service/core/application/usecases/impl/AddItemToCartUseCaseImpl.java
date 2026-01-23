package com.felipe.ecommerce_cart_service.core.application.usecases.impl;

import com.felipe.ecommerce_cart_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_cart_service.core.application.exceptions.CartItemAlreadyExistsException;
import com.felipe.ecommerce_cart_service.core.application.exceptions.CartNotFoundException;
import com.felipe.ecommerce_cart_service.core.application.gateway.CartGateway;
import com.felipe.ecommerce_cart_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_cart_service.core.application.usecases.AddItemToCartUseCase;
import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.core.domain.CartItem;

import java.util.Optional;
import java.util.UUID;

public class AddItemToCartUseCaseImpl implements AddItemToCartUseCase {
  private final CartGateway cartGateway;
  private final CustomerGateway customerGateway;

  public AddItemToCartUseCaseImpl(CartGateway cartGateway, CustomerGateway customerGateway) {
    this.cartGateway = cartGateway;
    this.customerGateway = customerGateway;
  }

  @Override
  public CartItem execute(UUID productId, int quantity, String customerEmail) {
    final CustomerProfileDTO customerProfile = this.customerGateway.fetchAuthCustomerProfile(customerEmail);
    final UUID customerId = UUID.fromString(customerProfile.id());

    // Find the authenticated customer cart
    final Cart foundCart = this.cartGateway.findCartByCustomerId(customerId)
      .orElseThrow(() -> new CartNotFoundException("Carrinho do cliente de id '" + customerId + "' não encontrado"));

    // Check if the product is already in the cart
    Optional<CartItem> existingCartItem = this.cartGateway.findCartItemByProductIdAndCartId(productId, foundCart.getId());
    if(existingCartItem.isPresent()) {
      throw new CartItemAlreadyExistsException("O produto de id '" + productId + "' já está no carrinho");
    }

    return this.cartGateway.addItemToCart(foundCart, productId, quantity);
  }
}
