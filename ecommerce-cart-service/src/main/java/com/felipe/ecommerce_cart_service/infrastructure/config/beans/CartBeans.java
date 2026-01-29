package com.felipe.ecommerce_cart_service.infrastructure.config.beans;

import com.felipe.ecommerce_cart_service.core.application.gateway.CartGateway;
import com.felipe.ecommerce_cart_service.core.application.usecases.AddItemToCartUseCase;
import com.felipe.ecommerce_cart_service.core.application.usecases.CreateCartUseCase;
import com.felipe.ecommerce_cart_service.core.application.usecases.GetAllCartItemsUseCase;
import com.felipe.ecommerce_cart_service.core.application.usecases.GetCartItemByIdUseCase;
import com.felipe.ecommerce_cart_service.core.application.usecases.RemoveItemFromCartUseCase;
import com.felipe.ecommerce_cart_service.core.application.usecases.impl.AddItemToCartUseCaseImpl;
import com.felipe.ecommerce_cart_service.core.application.usecases.impl.CreateCartUseCaseImpl;
import com.felipe.ecommerce_cart_service.core.application.usecases.impl.GetAllCartItemsUseCaseImpl;
import com.felipe.ecommerce_cart_service.core.application.usecases.impl.GetCartItemByIdUseCaseImpl;
import com.felipe.ecommerce_cart_service.core.application.usecases.impl.RemoveItemFromCartUseCaseImpl;
import com.felipe.ecommerce_cart_service.infrastructure.external.CustomerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CartBeans {
  private final CartGateway cartGateway;
  private final CustomerService customerService;

  public CartBeans(CartGateway cartGateway, CustomerService customerService) {
    this.cartGateway = cartGateway;
    this.customerService = customerService;
  }

  @Bean
  public CreateCartUseCase createCartUseCase() {
    return new CreateCartUseCaseImpl(this.cartGateway);
  }

  @Bean
  public AddItemToCartUseCase addItemToCartUseCase() {
    return new AddItemToCartUseCaseImpl(this.cartGateway, this.customerService);
  }

  @Bean
  public RemoveItemFromCartUseCase removeItemFromCartUseCase() {
    return new RemoveItemFromCartUseCaseImpl(this.cartGateway, this.customerService);
  }

  @Bean
  public GetCartItemByIdUseCase getCartItemByIdUseCase() {
    return new GetCartItemByIdUseCaseImpl(this.cartGateway, this.customerService);
  }

  @Bean
  public GetAllCartItemsUseCase getAllCartItemsUseCase() {
    return new GetAllCartItemsUseCaseImpl(this.cartGateway, this.customerService);
  }
}
