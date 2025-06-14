package com.felipe.ecommerce_customer_service.core.application.usecases;

import com.felipe.ecommerce_customer_service.core.application.dtos.UpdateCustomerDTO;
import com.felipe.ecommerce_customer_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_customer_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_customer_service.core.application.usecases.impl.UpdateCustomerProfileUseCaseImpl;
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
public class UpdateCustomerUseCaseImplTest {

  @Mock
  private CustomerGateway customerGateway;
  private UpdateCustomerProfileUseCaseImpl updateCustomerProfileUseCase;
  private Customer customerMock;

  @BeforeEach
  void setUp() {
    this.updateCustomerProfileUseCase = new UpdateCustomerProfileUseCaseImpl(this.customerGateway);
    this.customerMock= new DataMock().getCustomer();
  }

  @Test
  @DisplayName("updateCustomerProfileSuccess - Should successfully update a customer profile")
  void updateCustomerProfileSuccess() {
    UpdateCustomerDTO customerDTO = new UpdateCustomerDTO(
      "Updated username", "Updated First", "Updated Last"
    );

    when(this.customerGateway.findByEmail(this.customerMock.getEmail())).thenReturn(Optional.of(this.customerMock));
    when(this.customerGateway.updateCustomer(this.customerMock, customerDTO)).thenReturn(this.customerMock);

    Customer updatedCustomer = this.updateCustomerProfileUseCase.execute(this.customerMock.getEmail(), customerDTO);

    assertThat(updatedCustomer.getId()).isEqualTo(this.customerMock.getId());
    assertThat(updatedCustomer.getUsername()).isEqualTo(this.customerMock.getUsername());
    assertThat(updatedCustomer.getEmail()).isEqualTo(this.customerMock.getEmail());
    assertThat(updatedCustomer.getFirstName()).isEqualTo(this.customerMock.getFirstName());
    assertThat(updatedCustomer.getLastName()).isEqualTo(this.customerMock.getLastName());
    assertThat(updatedCustomer.getCreatedAt()).isEqualTo(this.customerMock.getCreatedAt());
    assertThat(updatedCustomer.getUpdatedAt()).isEqualTo(this.customerMock.getUpdatedAt());

    verify(this.customerGateway, times(1)).findByEmail(this.customerMock.getEmail());
    verify(this.customerGateway, times(1)).updateCustomer(this.customerMock, customerDTO);
  }

  @Test
  @DisplayName("updateCustomerProfileFailsByCustomerNotFound - Should throw a DataNotFoundException if customer is not found")
  void updateCustomerProfileFailsByCustomerNotFound() {
    UpdateCustomerDTO customerDTO = new UpdateCustomerDTO(
      "Updated username", "Updated First", "Updated Last"
    );

    when(this.customerGateway.findByEmail(this.customerMock.getEmail())).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.updateCustomerProfileUseCase.execute(this.customerMock.getEmail(), customerDTO));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Cliente de email '%s' não encontrado", this.customerMock.getEmail());

    verify(this.customerGateway, times(1)).findByEmail(this.customerMock.getEmail());
    verify(this.customerGateway, never()).updateCustomer(any(Customer.class), any(UpdateCustomerDTO.class));
  }
}