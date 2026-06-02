package com.felipe.ecommerce_order_service.core.application.usecases;

import com.felipe.ecommerce_order_service.core.application.exceptions.OrderNotFoundException;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.GetOrderByIdUseCaseImpl;
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
class GetOrderByIdUseCaseImplTest {

  @Mock
  private OrderGateway orderGateway;

  private GetOrderByIdUseCase getOrderByIdUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.getOrderByIdUseCase = new GetOrderByIdUseCaseImpl(this.orderGateway);
  }

  @Test
  @DisplayName("getOrderByIdSuccess - Should successfully get an order by id and return it")
  void getOrderByIdSuccess() {
    Order order = this.dataMock.getOrdersDomain().getFirst();

    when(this.orderGateway.findOrderById(order.getId())).thenReturn(Optional.of(order));

    Order foundOrder = this.getOrderByIdUseCase.execute(order.getId());

    assertThat(foundOrder).usingRecursiveComparison().isEqualTo(order);
    verify(this.orderGateway, times(1)).findOrderById(order.getId());
  }

  @Test
  @DisplayName("getOrderByIdFailsByOrderNotFound - Should throw an OrderNotFoundException if an order is not found")
  void getOrderByIdFailsByOrderNotFound() {
    UUID orderId = this.dataMock.getOrdersDomain().getFirst().getId();

    when(this.orderGateway.findOrderById(orderId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.getOrderByIdUseCase.execute(orderId));

    assertThat(thrown)
      .isExactlyInstanceOf(OrderNotFoundException.class)
      .hasMessage("Pedido de id '%s' não encontrado", orderId);

    verify(this.orderGateway, times(1)).findOrderById(orderId);
  }
}