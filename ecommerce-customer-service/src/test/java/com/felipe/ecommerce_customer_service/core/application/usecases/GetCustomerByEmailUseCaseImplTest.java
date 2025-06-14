package com.felipe.ecommerce_customer_service.core.application.usecases;

import com.felipe.ecommerce_customer_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_customer_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_customer_service.core.application.usecases.impl.GetCustomerByEmailUseCaseImpl;
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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class GetCustomerByEmailUseCaseImplTest {

  @Mock
  private CustomerGateway customerGateway;
  private GetCustomerByEmailUseCaseImpl getCustomerByEmailUseCase;
  private Customer customerMock;

  @BeforeEach
  void setUp() {
    this.getCustomerByEmailUseCase = new GetCustomerByEmailUseCaseImpl(this.customerGateway);
    this.customerMock = new DataMock().getCustomer();
  }

  @Test
  @DisplayName("getCustomerByEmailSuccess - Should find a customer by email and return it")
  void getCustomerByEmailSuccess() {
    when(this.customerGateway.findByEmail(this.customerMock.getEmail())).thenReturn(Optional.of(this.customerMock));

    Customer foundCustomer = this.getCustomerByEmailUseCase.execute(this.customerMock.getEmail());

    assertThat(foundCustomer.getId()).isEqualTo(this.customerMock.getId());
    assertThat(foundCustomer.getUsername()).isEqualTo(this.customerMock.getUsername());
    assertThat(foundCustomer.getEmail()).isEqualTo(this.customerMock.getEmail());
    assertThat(foundCustomer.getFirstName()).isEqualTo(this.customerMock.getFirstName());
    assertThat(foundCustomer.getLastName()).isEqualTo(this.customerMock.getLastName());
    assertThat(foundCustomer.getCreatedAt()).isEqualTo(this.customerMock.getCreatedAt());
    assertThat(foundCustomer.getUpdatedAt()).isEqualTo(this.customerMock.getUpdatedAt());

    verify(this.customerGateway, times(1)).findByEmail(this.customerMock.getEmail());
  }

  @Test
  @DisplayName("getCustomerByEmailFailsByCustomerNotFound - Should throw a DataNotFoundException if customer is not found")
  void getCustomerByEmailFailsByCustomerNotFound() {
    when(this.customerGateway.findByEmail(this.customerMock.getEmail())).thenReturn(Optional.empty());

    Exception exception = catchException(() -> this.getCustomerByEmailUseCase.execute(this.customerMock.getEmail()));

    assertThat(exception)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Cliente de email '%s' não encontrado", this.customerMock.getEmail());

    verify(this.customerGateway, times(1)).findByEmail(this.customerMock.getEmail());
  }
}
