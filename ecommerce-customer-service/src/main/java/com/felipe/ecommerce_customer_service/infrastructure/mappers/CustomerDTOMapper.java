package com.felipe.ecommerce_customer_service.infrastructure.mappers;

import com.felipe.ecommerce_customer_service.core.domain.Customer;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CreateCustomerDTO;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CustomerResponseDTO;
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

  public CustomerResponseDTO toResponseDTO(Customer customer) {
    return new CustomerResponseDTO(
      customer.getId().toString(),
      customer.getEmail(),
      customer.getUsername(),
      customer.getFirstName(),
      customer.getLastName(),
      customer.getCreatedAt().toString(),
      customer.getUpdatedAt().toString()
    );
  }
}
