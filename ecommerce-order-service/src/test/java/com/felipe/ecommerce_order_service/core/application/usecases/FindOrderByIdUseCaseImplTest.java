package com.felipe.ecommerce_order_service.core.application.usecases;

import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.FindOrderByIdUseCaseImpl;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindOrderByIdUseCaseImplTest {

  @Mock
  private OrderGateway orderGateway;

  private FindOrderByIdUseCase findOrderByIdUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.findOrderByIdUseCase = new FindOrderByIdUseCaseImpl(this.orderGateway);
  }

  @Test
  @DisplayName("findOrderByIdSuccess - Should find an order by id and return an Optional of Order")
  void findOrderByIdSuccess() {
    Order order = this.dataMock.getOrdersDomain().getFirst();

    when(this.orderGateway.findOrderById(order.getId())).thenReturn(Optional.of(order));

    Optional<Order> foundOrder = this.findOrderByIdUseCase.execute(order.getId());

    assertThat(foundOrder).isPresent();
    assertThat(foundOrder.get()).usingRecursiveComparison().isEqualTo(order);
    verify(this.orderGateway, times(1)).findOrderById(order.getId());
  }
}