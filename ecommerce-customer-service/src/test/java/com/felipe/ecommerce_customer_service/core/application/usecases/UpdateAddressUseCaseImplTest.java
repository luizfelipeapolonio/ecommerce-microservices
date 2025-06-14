package com.felipe.ecommerce_customer_service.core.application.usecases;

import com.felipe.ecommerce_customer_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_customer_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_customer_service.core.application.usecases.impl.UpdateAddressUseCaseImpl;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UpdateAddressUseCaseImplTest {

  @Mock
  private CustomerGateway customerGateway;

  private UpdateAddressUseCaseImpl updateAddressUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.updateAddressUseCase = new UpdateAddressUseCaseImpl(this.customerGateway);
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("updateAddressSuccess - Should successfully update an address and return the updated customer")
  void updateAddressSuccess() {
    Customer customer = this.dataMock.getCustomer();
    Address address = this.dataMock.getAddress();

    Address updatedAddress = Address.mutate(address)
      .street("Rua Nova")
      .number("1234B")
      .complement("apartamento - 2º andar")
      .build();
    Customer customerWithUpdatedAddress = Customer.mutate(customer)
      .address(updatedAddress)
      .build();

    when(this.customerGateway.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
    when(this.customerGateway.updateAddress(customer, address)).thenReturn(customerWithUpdatedAddress);

    Customer updatedCustomer = this.updateAddressUseCase.execute(customer.getEmail(), address);

    assertThat(updatedCustomer.getId()).isEqualTo(customer.getId());
    assertThat(updatedCustomer.getUsername()).isEqualTo(customer.getUsername());
    assertThat(updatedCustomer.getEmail()).isEqualTo(customer.getEmail());
    assertThat(updatedCustomer.getFirstName()).isEqualTo(customer.getFirstName());
    assertThat(updatedCustomer.getLastName()).isEqualTo(customer.getLastName());
    assertThat(updatedCustomer.getCreatedAt()).isEqualTo(customer.getCreatedAt());
    assertThat(updatedCustomer.getUpdatedAt()).isEqualTo(customer.getUpdatedAt());
    assertThat(updatedCustomer.getAddress().getId()).isEqualTo(updatedAddress.getId());
    assertThat(updatedCustomer.getAddress().getStreet()).isEqualTo(updatedAddress.getStreet());
    assertThat(updatedCustomer.getAddress().getNumber()).isEqualTo(updatedAddress.getNumber());
    assertThat(updatedCustomer.getAddress().getComplement()).isEqualTo(updatedAddress.getComplement());
    assertThat(updatedCustomer.getAddress().getZipcode()).isEqualTo(updatedAddress.getZipcode());
    assertThat(updatedCustomer.getAddress().getDistrict()).isEqualTo(updatedAddress.getDistrict());
    assertThat(updatedCustomer.getAddress().getCity()).isEqualTo(updatedAddress.getCity());
    assertThat(updatedCustomer.getAddress().getState()).isEqualTo(updatedAddress.getState());
    assertThat(updatedCustomer.getAddress().getCountry()).isEqualTo(updatedAddress.getCountry());

    verify(this.customerGateway, times(1)).findByEmail(customer.getEmail());
    verify(this.customerGateway, times(1)).updateAddress(customer, address);
  }

  @Test
  @DisplayName("updateAddressFailsByCustomerNotFound - Should throw a DataNotFoundException if customer is not found")
  void updateAddressFailsByCustomerNotFound() {
    Customer customer = this.dataMock.getCustomer();
    Address address = this.dataMock.getAddress();

    when(this.customerGateway.findByEmail(customer.getEmail())).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.updateAddressUseCase.execute(customer.getEmail(), address));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Cliente de email '%s' não encontrado", customer.getEmail());

    verify(this.customerGateway, times(1)).findByEmail(customer.getEmail());
    verify(this.customerGateway, never()).updateAddress(any(Customer.class), any(Address.class));
  }
}
