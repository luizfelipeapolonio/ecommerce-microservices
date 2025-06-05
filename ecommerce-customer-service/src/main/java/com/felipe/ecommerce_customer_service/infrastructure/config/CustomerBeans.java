package com.felipe.ecommerce_customer_service.infrastructure.config;

import com.felipe.ecommerce_customer_service.core.application.gateway.AuthServerGateway;
import com.felipe.ecommerce_customer_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_customer_service.core.application.usecases.CreateCustomerUseCase;
import com.felipe.ecommerce_customer_service.core.application.usecases.impl.CreateCustomerUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerBeans {
  private final CustomerGateway customerGateway;
  private final AuthServerGateway authServerGateway;

  public CustomerBeans(CustomerGateway customerGateway, AuthServerGateway authServerGateway) {
    this.customerGateway = customerGateway;
    this.authServerGateway = authServerGateway;
  }

  @Bean
  public CreateCustomerUseCase createCustomerUseCase() {
    return new CreateCustomerUseCaseImpl(customerGateway, authServerGateway);
  }
}
