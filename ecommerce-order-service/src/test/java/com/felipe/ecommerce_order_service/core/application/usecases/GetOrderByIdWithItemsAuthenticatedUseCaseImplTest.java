package com.felipe.ecommerce_order_service.core.application.usecases;

import com.felipe.ecommerce_order_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_order_service.core.application.exceptions.OrderNotFoundException;
import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.GetOrderByIdWithItemsAuthenticatedUseCaseImpl;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetOrderByIdWithItemsAuthenticatedUseCaseImplTest {

  @Mock
  private OrderGateway orderGateway;

  @Mock
  private CustomerGateway customerGateway;

  private GetOrderByIdWithItemsUseCase getOrderByIdWithItemsUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.getOrderByIdWithItemsUseCase = new GetOrderByIdWithItemsAuthenticatedUseCaseImpl(this.orderGateway, this.customerGateway);
  }

  @Test
  @DisplayName("getOrderByIdWithItemsAuthenticatedSuccess - Should successfully get an order by id and customer id with items")
  void getOrderByIdWithItemsAuthenticatedSuccess() {
    CustomerProfileDTO customerProfile = this.dataMock.getCustomerProfileDTO();
    Order order = this.dataMock.getOrdersDomain().getFirst();
    UUID customerId = UUID.fromString(customerProfile.id());

    when(this.customerGateway.fetchAuthCustomerProfile(customerProfile.email())).thenReturn(customerProfile)
      .thenReturn(customerProfile);
    when(this.orderGateway.findOrderByIdAndCustomerIdWithItems(order.getId(), customerId))
      .thenReturn(Optional.of(order));

    Order foundOrder = this.getOrderByIdWithItemsUseCase.execute(order.getId(), customerProfile.email());

    assertThat(foundOrder).usingRecursiveComparison().isEqualTo(order);
    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(customerProfile.email());
    verify(this.orderGateway, times(1)).findOrderByIdAndCustomerIdWithItems(order.getId(), customerId);
  }

  @Test
  @DisplayName("getOrderByIdWithItemsAuthenticatedFailsByOrderNotFound - Should throw an OrderNotFoundException if order is not found")
  void getOrderByIdWithItemsAuthenticatedFailsByOrderNotFound() {
    CustomerProfileDTO customerProfile = this.dataMock.getCustomerProfileDTO();
    UUID customerId = UUID.fromString(customerProfile.id());
    UUID orderId = this.dataMock.getOrdersDomain().getFirst().getId();

    when(this.customerGateway.fetchAuthCustomerProfile(customerProfile.email())).thenReturn(customerProfile);
    when(this.orderGateway.findOrderByIdAndCustomerIdWithItems(orderId, customerId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.getOrderByIdWithItemsUseCase.execute(orderId, customerProfile.email()));

    assertThat(thrown)
      .isExactlyInstanceOf(OrderNotFoundException.class)
      .hasMessage("Pedido de id '%s' do usuário de e-mail '%s' não encontrado", orderId, customerProfile.email());

    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(customerProfile.email());
    verify(this.orderGateway, times(1)).findOrderByIdAndCustomerIdWithItems(orderId, customerId);
  }
}