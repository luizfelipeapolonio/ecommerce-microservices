package com.felipe.ecommerce_cart_service.core.application.usecases.impl;

import com.felipe.ecommerce_cart_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_cart_service.core.application.exceptions.CartNotFoundException;
import com.felipe.ecommerce_cart_service.core.application.gateway.CartGateway;
import com.felipe.ecommerce_cart_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_cart_service.core.application.usecases.GetAllCartItemsUseCase;
import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.core.domain.CartItem;

import java.util.List;
import java.util.UUID;

public class GetAllCartItemsUseCaseImpl implements GetAllCartItemsUseCase {
  private final CartGateway cartGateway;
  private final CustomerGateway customerGateway;

  public GetAllCartItemsUseCaseImpl(CartGateway cartGateway, CustomerGateway customerGateway) {
    this.cartGateway = cartGateway;
    this.customerGateway = customerGateway;
  }

  @Override
  public List<CartItem> execute(String customerEmail) {
    CustomerProfileDTO customerProfile = this.customerGateway.fetchAuthCustomerProfile(customerEmail);
    UUID customerId = UUID.fromString(customerProfile.id());
    Cart foundCart = this.cartGateway.findCartByCustomerId(customerId)
      .orElseThrow(() -> new CartNotFoundException("Carrinho do cliente de id '" + customerId + "' não encontrado"));
    return foundCart.getItems();
  }
}
