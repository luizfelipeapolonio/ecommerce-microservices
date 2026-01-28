package com.felipe.ecommerce_cart_service.core.application.usecases.impl;

import com.felipe.ecommerce_cart_service.core.application.dtos.CartItemDTO;
import com.felipe.ecommerce_cart_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_cart_service.core.application.exceptions.CartItemAlreadyExistsException;
import com.felipe.ecommerce_cart_service.core.application.exceptions.CartNotFoundException;
import com.felipe.ecommerce_cart_service.core.application.gateway.CartGateway;
import com.felipe.ecommerce_cart_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_cart_service.core.application.usecases.AddItemToCartUseCase;
import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.core.domain.CartItem;

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

    boolean isItemAlreadyInCart = foundCart.getItems()
      .stream()
      .anyMatch(item -> item.getProductId().equals(productId));

    if(isItemAlreadyInCart) {
      throw new CartItemAlreadyExistsException("O produto de id '" + productId + "' já está no carrinho");
    }

    CartItemDTO itemDTO = this.cartGateway.addItemToCart(foundCart, productId, quantity);
    return itemDTO.cart().getItems().get(itemDTO.itemIndex());
  }
}
