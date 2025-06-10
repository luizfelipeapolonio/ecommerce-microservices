package com.felipe.ecommerce_customer_service.infrastructure.mappers;

import com.felipe.ecommerce_customer_service.core.domain.Customer;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CreateCustomerDTO;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CustomerDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomerDTOMapper {
  private final AddressDTOMapper addressMapper;

  public CustomerDTOMapper(AddressDTOMapper addressMapper) {
    this.addressMapper = addressMapper;
  }

  public Customer toDomain(CreateCustomerDTO customerDTO) {
    return Customer.builder()
      .email(customerDTO.email())
      .firstName(customerDTO.firstName())
      .lastName(customerDTO.lastName())
      .username(customerDTO.username())
      .build();
  }

  public CustomerDTO toDTO(Customer customer) {
    return new CustomerDTO(
      customer.getId().toString(),
      customer.getEmail(),
      customer.getUsername(),
      customer.getFirstName(),
      customer.getLastName(),
      customer.getCreatedAt().toString(),
      customer.getUpdatedAt().toString()
    );
  }

  public CustomerProfileDTO toProfileDTO(Customer customer) {
    return new CustomerProfileDTO(
      customer.getId().toString(),
      customer.getEmail(),
      customer.getUsername(),
      customer.getFirstName(),
      customer.getLastName(),
      customer.getCreatedAt().toString(),
      customer.getUpdatedAt().toString(),
      customer.getAddress() != null ? this.addressMapper.toDto(customer.getAddress()) : null
    );
  }
}
