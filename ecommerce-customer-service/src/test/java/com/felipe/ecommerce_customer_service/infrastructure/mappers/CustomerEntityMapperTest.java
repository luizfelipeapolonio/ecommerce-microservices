package com.felipe.ecommerce_customer_service.infrastructure.mappers;

import com.felipe.ecommerce_customer_service.core.domain.Customer;
import com.felipe.ecommerce_customer_service.infrastructure.persistence.entities.CustomerEntity;
import com.felipe.ecommerce_customer_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CustomerEntityMapperTest {

  @Spy
  private AddressEntityMapper addressMapper;

  @InjectMocks
  private CustomerEntityMapper customerMapper;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  void shouldConvertCustomerToCustomerEntity() {
    Customer customer = Customer.mutate(this.dataMock.getCustomer())
      .address(this.dataMock.getAddress())
      .build();

    CustomerEntity convertedEntity = this.customerMapper.toEntity(customer);

    assertThat(convertedEntity.getId()).isEqualTo(customer.getId());
    assertThat(convertedEntity.getEmail()).isEqualTo(customer.getEmail());
    assertThat(convertedEntity.getUsername()).isEqualTo(customer.getUsername());
    assertThat(convertedEntity.getFirstName()).isEqualTo(customer.getFirstName());
    assertThat(convertedEntity.getLastName()).isEqualTo(customer.getLastName());
    assertThat(convertedEntity.getCreatedAt()).isEqualTo(customer.getCreatedAt());
    assertThat(convertedEntity.getUpdatedAt()).isEqualTo(customer.getUpdatedAt());
    assertThat(convertedEntity.getAddress()).isNotNull();

    verify(this.addressMapper, times(1)).toEntity(customer.getAddress());
  }

  @Test
  void shouldConvertCustomerEntityToCustomer() {
    CustomerEntity customerEntity = CustomerEntity.mutate(this.dataMock.getCustomerEntity())
      .address(this.dataMock.getAddressEntity())
      .build();

    Customer convertedCustomer = this.customerMapper.toDomain(customerEntity);

    assertThat(convertedCustomer.getId()).isEqualTo(customerEntity.getId());
    assertThat(convertedCustomer.getEmail()).isEqualTo(customerEntity.getEmail());
    assertThat(convertedCustomer.getUsername()).isEqualTo(customerEntity.getUsername());
    assertThat(convertedCustomer.getFirstName()).isEqualTo(customerEntity.getFirstName());
    assertThat(convertedCustomer.getLastName()).isEqualTo(customerEntity.getLastName());
    assertThat(convertedCustomer.getCreatedAt()).isEqualTo(customerEntity.getCreatedAt());
    assertThat(convertedCustomer.getUpdatedAt()).isEqualTo(customerEntity.getUpdatedAt());
    assertThat(convertedCustomer.getAddress()).isNotNull();

    verify(this.addressMapper, times(1)).toDomain(customerEntity.getAddress());
  }
}
