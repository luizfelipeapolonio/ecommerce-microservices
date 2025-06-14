package com.felipe.ecommerce_customer_service.core.application.usecases;

import com.felipe.ecommerce_customer_service.core.application.dtos.CustomerAuthDataDTO;
import com.felipe.ecommerce_customer_service.core.application.exceptions.EmailAlreadyExistsException;
import com.felipe.ecommerce_customer_service.core.application.gateway.AuthServerGateway;
import com.felipe.ecommerce_customer_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_customer_service.core.application.usecases.impl.CreateCustomerUseCaseImpl;
import com.felipe.ecommerce_customer_service.core.domain.Customer;
import com.felipe.ecommerce_customer_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.any;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CreateCustomerUseCaseImplTest {

  @Mock
  private CustomerGateway customerGateway;

  @Mock
  private AuthServerGateway authServerGateway;

  public CreateCustomerUseCaseImpl createCustomerUseCase;
  private Customer customerMock;

  @BeforeEach
  void setUp() {
    this.createCustomerUseCase = new CreateCustomerUseCaseImpl(this.customerGateway, this.authServerGateway);
    this.customerMock = new DataMock().getCustomer();
  }

  @Test
  @DisplayName("createCustomerSuccess - Should successfully create a new customer and return it")
  void createCustomerSuccess() {
    when(this.customerGateway.findByEmail(this.customerMock.getEmail())).thenReturn(Optional.empty());
    doNothing().when(this.authServerGateway).registerCustomer(any(CustomerAuthDataDTO.class));
    when(this.customerGateway.createCustomer(this.customerMock)).thenReturn(this.customerMock);

    Customer createdCustomer = this.createCustomerUseCase.execute(this.customerMock, "1234");

    assertThat(createdCustomer.getId()).isEqualTo(this.customerMock.getId());
    assertThat(createdCustomer.getUsername()).isEqualTo(this.customerMock.getUsername());
    assertThat(createdCustomer.getEmail()).isEqualTo(this.customerMock.getEmail());
    assertThat(createdCustomer.getFirstName()).isEqualTo(this.customerMock.getFirstName());
    assertThat(createdCustomer.getLastName()).isEqualTo(this.customerMock.getLastName());
    assertThat(createdCustomer.getCreatedAt()).isEqualTo(this.customerMock.getCreatedAt());
    assertThat(createdCustomer.getUpdatedAt()).isEqualTo(this.customerMock.getUpdatedAt());

    verify(this.customerGateway, times(1)).findByEmail(this.customerMock.getEmail());
    verify(this.authServerGateway, times(1)).registerCustomer(any(CustomerAuthDataDTO.class));
    verify(this.customerGateway, times(1)).createCustomer(this.customerMock);
  }

  @Test
  @DisplayName("createCustomerFailsByEmailAlreadyExists - Should throw an EmailAlreadyExistsException")
  void createCustomerFailsByEmailAlreadyExists() {
    when(this.customerGateway.findByEmail(this.customerMock.getEmail())).thenReturn(Optional.of(this.customerMock));

    Exception thrown = catchException(() -> this.createCustomerUseCase.execute(this.customerMock, "1234"));

    assertThat(thrown)
      .isExactlyInstanceOf(EmailAlreadyExistsException.class)
      .hasMessage("E-mail '%s' já cadastrado", this.customerMock.getEmail());

    verify(this.customerGateway, times(1)).findByEmail(this.customerMock.getEmail());
    verify(this.authServerGateway, never()).registerCustomer(any(CustomerAuthDataDTO.class));
    verify(this.customerGateway, never()).createCustomer(any(Customer.class));
  }
}
