package com.felipe.ecommerce_customer_service.infrastructure.mappers;

import com.felipe.ecommerce_customer_service.core.domain.Customer;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CreateCustomerDTO;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CustomerDTO;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_customer_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CustomerDTOMapperTest {

  @Spy
  private AddressDTOMapper addressMapper;

  @InjectMocks
  private CustomerDTOMapper customerMapper;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  void shouldConvertCreateCustomerDTOToCustomer() {
    CreateCustomerDTO customerDTO = new CreateCustomerDTO(
      "test@email.com", "User", "Test", "Customer", "1234"
    );

    Customer convertedCustomer = this.customerMapper.toDomain(customerDTO);

    assertThat(convertedCustomer.getEmail()).isEqualTo(customerDTO.email());
    assertThat(convertedCustomer.getFirstName()).isEqualTo(customerDTO.firstName());
    assertThat(convertedCustomer.getLastName()).isEqualTo(customerDTO.lastName());
    assertThat(convertedCustomer.getUsername()).isEqualTo(customerDTO.username());
  }

  @Test
  void shouldConvertCustomerToCustomerDTO() {
    Customer customer = this.dataMock.getCustomer();

    CustomerDTO convertedCustomerDTO = this.customerMapper.toDTO(customer);

    assertThat(convertedCustomerDTO.id()).isEqualTo(customer.getId().toString());
    assertThat(convertedCustomerDTO.email()).isEqualTo(customer.getEmail());
    assertThat(convertedCustomerDTO.username()).isEqualTo(customer.getUsername());
    assertThat(convertedCustomerDTO.firstName()).isEqualTo(customer.getFirstName());
    assertThat(convertedCustomerDTO.lastName()).isEqualTo(customer.getLastName());
    assertThat(convertedCustomerDTO.createdAt()).isEqualTo(customer.getCreatedAt().toString());
    assertThat(convertedCustomerDTO.updatedAt()).isEqualTo(customer.getUpdatedAt().toString());
  }

  @Test
  void shouldConvertCustomerToProfileDTO() {
    Customer customer = Customer.mutate(this.dataMock.getCustomer())
      .address(this.dataMock.getAddress())
      .build();

    CustomerProfileDTO convertedCustomer = this.customerMapper.toProfileDTO(customer);

    assertThat(convertedCustomer.id()).isEqualTo(customer.getId().toString());
    assertThat(convertedCustomer.email()).isEqualTo(customer.getEmail());
    assertThat(convertedCustomer.username()).isEqualTo(customer.getUsername());
    assertThat(convertedCustomer.firstName()).isEqualTo(customer.getFirstName());
    assertThat(convertedCustomer.lastName()).isEqualTo(customer.getLastName());
    assertThat(convertedCustomer.createdAt()).isEqualTo(customer.getCreatedAt().toString());
    assertThat(convertedCustomer.updatedAt()).isEqualTo(customer.getUpdatedAt().toString());
    assertThat(convertedCustomer.address()).isNotNull();

    verify(this.addressMapper, times(1)).toDto(customer.getAddress());
  }
}
