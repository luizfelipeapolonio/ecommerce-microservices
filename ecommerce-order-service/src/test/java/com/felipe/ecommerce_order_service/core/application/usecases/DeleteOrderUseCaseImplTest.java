package com.felipe.ecommerce_order_service.core.application.usecases;

import com.felipe.ecommerce_order_service.core.application.exceptions.OrderNotFoundException;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.DeleteOrderUseCaseImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteOrderUseCaseImplTest {

  @Mock
  private OrderGateway orderGateway;

  private DeleteOrderUseCase deleteOrderUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.deleteOrderUseCase = new DeleteOrderUseCaseImpl(this.orderGateway);
  }

  @Test
  @DisplayName("deleteOrderSuccess - Should successfully delete an order")
  void deleteOrderSuccess() {
    Order order = this.dataMock.getOrdersDomain().getFirst();

    when(this.orderGateway.findOrderById(order.getId())).thenReturn(Optional.of(order));
    doNothing().when(this.orderGateway).deleteOrder(order.getId());

    Order deletedOrder = this.deleteOrderUseCase.execute(order.getId());

    assertThat(deletedOrder).usingRecursiveComparison().isEqualTo(order);

    verify(this.orderGateway, times(1)).findOrderById(order.getId());
    verify(this.orderGateway, times(1)).deleteOrder(order.getId());
  }

  @Test
  @DisplayName("deleteOrderFailsByOrderNotFound - Should throw an OrderNotFoundException if the order is not found")
  void deleteOrderFailsByOrderNotFound() {
    UUID orderId = this.dataMock.getOrdersDomain().getFirst().getId();

    when(this.orderGateway.findOrderById(orderId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.deleteOrderUseCase.execute(orderId));

    assertThat(thrown)
      .isExactlyInstanceOf(OrderNotFoundException.class)
      .hasMessage("Pedido de id '%s' não encontrado", orderId);

    verify(this.orderGateway, times(1)).findOrderById(orderId);
    verify(this.orderGateway, never()).deleteOrder(any(UUID.class));
  }
}