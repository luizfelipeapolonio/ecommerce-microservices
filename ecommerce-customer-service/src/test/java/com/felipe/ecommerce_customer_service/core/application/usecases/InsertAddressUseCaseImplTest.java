package com.felipe.ecommerce_customer_service.core.application.usecases;

import com.felipe.ecommerce_customer_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_customer_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_customer_service.core.application.usecases.impl.InsertAddressUseCaseImpl;
import com.felipe.ecommerce_customer_service.core.domain.Address;
import com.felipe.ecommerce_customer_service.core.domain.Customer;
import com.felipe.ecommerce_customer_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class InsertAddressUseCaseImplTest {

  @Mock
  private CustomerGateway customerGateway;

  private InsertAddressUseCaseImpl insertAddressUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.insertAddressUseCase = new InsertAddressUseCaseImpl(this.customerGateway);
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("insertAddressSuccess - Should successfully insert an address to a customer")
  void insertAddressSuccess() {
    Customer customer = this.dataMock.getCustomer();
    Address address = this.dataMock.getAddress();
    Customer customerWithAddress = Customer.mutate(customer)
      .address(address)
      .build();

    when(this.customerGateway.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
    when(this.customerGateway.insertAddress(customer, address)).thenReturn(customerWithAddress);

    Customer updatedCustomer = this.insertAddressUseCase.execute(customer.getEmail(), address);

    assertThat(updatedCustomer.getId()).isEqualTo(customer.getId());
    assertThat(updatedCustomer.getUsername()).isEqualTo(customer.getUsername());
    assertThat(updatedCustomer.getEmail()).isEqualTo(customer.getEmail());
    assertThat(updatedCustomer.getFirstName()).isEqualTo(customer.getFirstName());
    assertThat(updatedCustomer.getLastName()).isEqualTo(customer.getLastName());
    assertThat(updatedCustomer.getCreatedAt()).isEqualTo(customer.getCreatedAt());
    assertThat(updatedCustomer.getUpdatedAt()).isEqualTo(customer.getUpdatedAt());
    assertThat(updatedCustomer.getAddress().getId()).isEqualTo(address.getId());
    assertThat(updatedCustomer.getAddress().getStreet()).isEqualTo(address.getStreet());
    assertThat(updatedCustomer.getAddress().getNumber()).isEqualTo(address.getNumber());
    assertThat(updatedCustomer.getAddress().getComplement()).isEqualTo(address.getComplement());
    assertThat(updatedCustomer.getAddress().getZipcode()).isEqualTo(address.getZipcode());
    assertThat(updatedCustomer.getAddress().getDistrict()).isEqualTo(address.getDistrict());
    assertThat(updatedCustomer.getAddress().getCity()).isEqualTo(address.getCity());
    assertThat(updatedCustomer.getAddress().getState()).isEqualTo(address.getState());
    assertThat(updatedCustomer.getAddress().getCountry()).isEqualTo(address.getCountry());

    verify(this.customerGateway, times(1)).findByEmail(customer.getEmail());
    verify(this.customerGateway, times(1)).insertAddress(customer, address);
  }

  @Test
  @DisplayName("insertAddressFailsByCustomerNotFound - Should throw a DataNotFoundException if customer is not found")
  void insertAddressFailsByCustomerNotFound() {
    Customer customer = this.dataMock.getCustomer();
    Address address = this.dataMock.getAddress();

    when(this.customerGateway.findByEmail(customer.getEmail())).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.insertAddressUseCase.execute(customer.getEmail(), address));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Cliente de email '%s' não encontrado", customer.getEmail());

    verify(this.customerGateway, times(1)).findByEmail(customer.getEmail());
    verify(this.customerGateway, never()).insertAddress(any(Customer.class), any(Address.class));
  }
}
