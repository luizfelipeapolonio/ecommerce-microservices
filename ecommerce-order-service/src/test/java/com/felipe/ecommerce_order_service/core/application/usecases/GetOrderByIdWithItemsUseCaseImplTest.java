package com.felipe.ecommerce_order_service.core.application.usecases;

import com.felipe.ecommerce_order_service.core.application.exceptions.OrderNotFoundException;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.GetOrderByIdWithItemsUseCaseImpl;
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
class GetOrderByIdWithItemsUseCaseImplTest {

  @Mock
  private OrderGateway orderGateway;

  private GetOrderByIdWithItemsUseCase getOrderByIdWithItemsUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.getOrderByIdWithItemsUseCase = new GetOrderByIdWithItemsUseCaseImpl(this.orderGateway);
  }

  @Test
  @DisplayName("getOrderByIdWithItemsSuccess - Should successfully get an order by id with items")
  void getOrderByIdWithItemsSuccess() {
    Order order = this.dataMock.getOrdersDomain().getFirst();
    String customerEmail = "test@email.com";

    when(this.orderGateway.findOrderByIdWithItems(order.getId())).thenReturn(Optional.of(order));

    Order foundOrder = this.getOrderByIdWithItemsUseCase.execute(order.getId(), customerEmail);

    assertThat(foundOrder).usingRecursiveComparison().isEqualTo(order);
    verify(this.orderGateway, times(1)).findOrderByIdWithItems(order.getId());
  }

  @Test
  @DisplayName("getOrderByIdWithItemsFailsByOrderNotFound - Should throw an OrderNotFoundException if order is not found")
  void getOrderByIdWithItemsFailsByOrderNotFound() {
    String customerEmail = "test@email.com";
    UUID orderId = this.dataMock.getOrdersDomain().getFirst().getId();

    when(this.orderGateway.findOrderByIdWithItems(orderId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.getOrderByIdWithItemsUseCase.execute(orderId, customerEmail));

    assertThat(thrown)
      .isExactlyInstanceOf(OrderNotFoundException.class)
      .hasMessage("Pedido de id '%s' não encontrado", orderId);

    verify(this.orderGateway, times(1)).findOrderByIdWithItems(orderId);
  }
}