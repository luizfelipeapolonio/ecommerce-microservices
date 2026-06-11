package com.felipe.ecommerce_order_service.core.application.usecases;

import com.felipe.ecommerce_order_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.GetAllCustomerOrdersUseCaseImpl;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllCustomerOrdersUseCaseImplTest {

  @Mock
  private OrderGateway orderGateway;

  @Mock
  private CustomerGateway customerGateway;

  private GetAllCustomerOrdersUseCase getAllCustomerOrdersUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.getAllCustomerOrdersUseCase = new GetAllCustomerOrdersUseCaseImpl(this.orderGateway, this.customerGateway);
  }

  @Test
  @DisplayName("getAllCustomerOrdersSuccess - Should successfully get all customer orders")
  void getAllCustomerOrdersSuccess() {
    CustomerProfileDTO customerProfile = this.dataMock.getCustomerProfileDTO();
    UUID customerId = UUID.fromString(customerProfile.id());
    List<Order> ordersDomain = this.dataMock.getOrdersDomain();
    ordersDomain.forEach(order -> order.addItem(this.dataMock.getOrderItemsDomain().getFirst()));

    when(this.customerGateway.fetchAuthCustomerProfile(customerProfile.email())).thenReturn(customerProfile);
    when(this.orderGateway.getAllCustomerOrders(customerId)).thenReturn(ordersDomain);

    List<Order> orders = this.getAllCustomerOrdersUseCase.execute(customerProfile.email());

    assertThat(orders).isNotEmpty();
    assertThat(orders.size()).isEqualTo(1);
    assertThat(orders.getFirst()).usingRecursiveComparison().isEqualTo(ordersDomain.getFirst());
    assertThat(orders.getFirst().getItems().getFirst()).usingRecursiveComparison()
      .isEqualTo(ordersDomain.getFirst().getItems().getFirst());

    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(customerProfile.email());
    verify(this.orderGateway, times(1)).getAllCustomerOrders(customerId);
  }
}