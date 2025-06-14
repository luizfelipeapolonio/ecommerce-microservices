package com.felipe.ecommerce_customer_service.testutils;

import com.felipe.ecommerce_customer_service.core.domain.Address;
import com.felipe.ecommerce_customer_service.core.domain.Customer;
import com.felipe.ecommerce_customer_service.infrastructure.persistence.entities.AddressEntity;
import com.felipe.ecommerce_customer_service.infrastructure.persistence.entities.CustomerEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public class DataMock {
  public final Customer customerMock;
  public final CustomerEntity customerEntityMock;
  public final Address addressMock;
  public final AddressEntity addressEntityMock;

  public DataMock() {
    this.customerMock = Customer.builder()
      .id(UUID.fromString("c3d036fe-dcba-4e18-a36f-5d5ea15f9b11"))
      .username("Customer")
      .firstName("User")
      .lastName("Test")
      .email("test@email.com")
      .createdAt(LocalDateTime.parse("2025-06-10T20:58:38.886106328"))
      .updatedAt(LocalDateTime.parse("2025-06-10T20:58:38.886106328"))
      .build();

    this.customerEntityMock = CustomerEntity.builder()
      .id(UUID.fromString("c3d036fe-dcba-4e18-a36f-5d5ea15f9b11"))
      .username("Customer")
      .firstName("User")
      .lastName("Test")
      .email("test@email.com")
      .createdAt(LocalDateTime.parse("2025-06-10T20:58:38.886106328"))
      .updatedAt(LocalDateTime.parse("2025-06-10T20:58:38.886106328"))
      .build();

    this.addressMock = Address.builder()
      .id(1L)
      .street("Rua das Alamedas")
      .number("12A")
      .complement("casa")
      .zipcode("12345-678")
      .district("Jardim do Ypês")
      .city("São Paulo")
      .state("São Paulo")
      .country("Brasil")
      .build();

    this.addressEntityMock = AddressEntity.builder()
      .id(1L)
      .street("Rua das Alamedas")
      .number("12A")
      .complement("casa")
      .zipcode("12345-678")
      .district("Jardim do Ypês")
      .city("São Paulo")
      .state("São Paulo")
      .country("Brasil")
      .build();
  }

  public Customer getCustomer() {
    return this.customerMock;
  }

  public CustomerEntity getCustomerEntity() {
    return this.customerEntityMock;
  }

  public Address getAddress() {
    return this.addressMock;
  }

  public AddressEntity getAddressEntity() {
    return this.addressEntityMock;
  }
}
