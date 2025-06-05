package com.felipe.ecommerce_customer_service.infrastructure.mappers;

import com.felipe.ecommerce_customer_service.core.domain.Customer;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CreateCustomerDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomerDTOMapper {

  public Customer toDomain(CreateCustomerDTO customerDTO) {
    return Customer.builder()
      .email(customerDTO.email())
      .firstName(customerDTO.firstName())
      .lastName(customerDTO.lastName())
      .username(customerDTO.username())
      .build();
  }
}
