package com.felipe.ecommerce_cart_service.infrastructure.config.beans;

import com.felipe.ecommerce_cart_service.core.application.gateway.CartGateway;
import com.felipe.ecommerce_cart_service.core.application.usecases.CreateCartUseCase;
import com.felipe.ecommerce_cart_service.core.application.usecases.impl.CreateCartUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CartBeans {
  private final CartGateway cartGateway;

  public CartBeans(CartGateway cartGateway) {
    this.cartGateway = cartGateway;
  }

  @Bean
  public CreateCartUseCase createCartUseCase() {
    return new CreateCartUseCaseImpl(this.cartGateway);
  }
}
