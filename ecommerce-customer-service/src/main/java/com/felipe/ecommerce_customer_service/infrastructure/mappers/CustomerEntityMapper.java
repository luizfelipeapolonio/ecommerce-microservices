package com.felipe.ecommerce_customer_service.infrastructure.mappers;

import com.felipe.ecommerce_customer_service.core.domain.Customer;
import com.felipe.ecommerce_customer_service.infrastructure.persistence.entities.CustomerEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerEntityMapper {
  private final AddressEntityMapper addressMapper;

  public CustomerEntityMapper(AddressEntityMapper addressMapper) {
    this.addressMapper = addressMapper;
  }

  public CustomerEntity toEntity(Customer customer) {
    return CustomerEntity.builder()
      .id(customer.getId())
      .email(customer.getEmail())
      .username(customer.getUsername())
      .firstName(customer.getFirstName())
      .lastName(customer.getLastName())
      .createdAt(customer.getCreatedAt())
      .updatedAt(customer.getUpdatedAt())
      .address(customer.getAddress() != null ? this.addressMapper.toEntity(customer.getAddress()) : null)
      .build();
  }

  public Customer toDomain(CustomerEntity entity) {
    return Customer.builder()
      .id(entity.getId())
      .email(entity.getEmail())
      .username(entity.getUsername())
      .firstName(entity.getLastName())
      .lastName(entity.getLastName())
      .createdAt(entity.getCreatedAt())
      .updatedAt(entity.getUpdatedAt())
      .address(entity.getAddress() != null ? this.addressMapper.toDomain(entity.getAddress()) : null)
      .build();
  }
}
