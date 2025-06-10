package com.felipe.ecommerce_customer_service.infrastructure.config;

import com.felipe.ecommerce_customer_service.core.application.gateway.AuthServerGateway;
import com.felipe.ecommerce_customer_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_customer_service.core.application.usecases.CreateCustomerUseCase;
import com.felipe.ecommerce_customer_service.core.application.usecases.GetCustomerByEmailUseCase;
import com.felipe.ecommerce_customer_service.core.application.usecases.InsertAddressUseCase;
import com.felipe.ecommerce_customer_service.core.application.usecases.UpdateAddressUseCase;
import com.felipe.ecommerce_customer_service.core.application.usecases.UpdateCustomerProfileUseCase;
import com.felipe.ecommerce_customer_service.core.application.usecases.impl.CreateCustomerUseCaseImpl;
import com.felipe.ecommerce_customer_service.core.application.usecases.impl.GetCustomerByEmailUseCaseImpl;
import com.felipe.ecommerce_customer_service.core.application.usecases.impl.InsertAddressUseCaseImpl;
import com.felipe.ecommerce_customer_service.core.application.usecases.impl.UpdateAddressUseCaseImpl;
import com.felipe.ecommerce_customer_service.core.application.usecases.impl.UpdateCustomerProfileUseCaseImpl;
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

  @Bean
  public GetCustomerByEmailUseCase getCustomerByEmailUseCase() {
    return new GetCustomerByEmailUseCaseImpl(customerGateway);
  }

  @Bean
  public UpdateCustomerProfileUseCase updateCustomerProfileUseCase() {
    return new UpdateCustomerProfileUseCaseImpl(customerGateway);
  }

  @Bean
  public InsertAddressUseCase insertAddressUseCase() {
    return new InsertAddressUseCaseImpl(customerGateway);
  }

  @Bean
  public UpdateAddressUseCase updateAddressUseCase() {
    return new UpdateAddressUseCaseImpl(customerGateway);
  }
}
