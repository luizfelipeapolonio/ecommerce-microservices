package com.felipe.ecommerce_order_service.core.application.usecases;

import com.felipe.ecommerce_order_service.core.application.dtos.UpdateOrderDTO;
import com.felipe.ecommerce_order_service.core.application.dtos.UpdateProductDTO;
import com.felipe.ecommerce_order_service.core.application.exceptions.OrderNotFoundException;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.UpdateOrderUseCaseImpl;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.core.domain.OrderItem;
import com.felipe.ecommerce_order_service.core.domain.enums.OrderStatus;
import com.felipe.ecommerce_order_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateOrderUseCaseImplTest {

  @Mock
  private OrderGateway orderGateway;

  private UpdateOrderUseCase updateOrderUseCase;
  private DataMock dataMock;
  private UpdateOrderDTO updateOrderDTO;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.updateOrderUseCase = new UpdateOrderUseCaseImpl(this.orderGateway);
    this.updateOrderDTO = new UpdateOrderDTO()
      .updateStatus(OrderStatus.FINISHED)
      .updateCheckoutUrl("https://checkoutUrl.com")
      .updateInvoiceUrl("https://invoiceUrl.com")
      .updateOrderPrice("120.00")
      .updateCouponValue("20.00")
      .updateShippingFee("30.00")
      .updateWithCoupon(false)
      .updateProduct(new UpdateProductDTO(
        UUID.fromString("0008f5ca-70ab-48c6-a9c5-976b0191bbf4"),
        "Product 1 updated",
        new BigDecimal("180.00")
      ));
  }

  @Test
  @DisplayName("updateOrderSuccess - Should successfully update an order")
  void updateOrderSuccess() {
    Order order = this.dataMock.getOrdersDomain().getFirst();
    OrderItem orderItem = this.dataMock.getOrderItemsDomain().getFirst();
    order.addItem(orderItem);

    when(this.orderGateway.findOrderById(order.getId())).thenReturn(Optional.of(order));

    this.updateOrderUseCase.execute(order.getId(), this.updateOrderDTO);

    verify(this.orderGateway, times(1))
      .updateOrder(assertArg(updatedOrder -> {
        assertThat(updatedOrder.getStatus()).isEqualTo(this.updateOrderDTO.status().getText());
        assertThat(updatedOrder.getCheckoutUrl()).isEqualTo(this.updateOrderDTO.checkoutUrl());
        assertThat(updatedOrder.getInvoiceUrl()).isEqualTo(this.updateOrderDTO.invoiceUrl());
        assertThat(updatedOrder.getOrderPrice()).isEqualTo(this.updateOrderDTO.orderPrice());
        assertThat(updatedOrder.getCouponValue()).isEqualTo(this.updateOrderDTO.couponValue());
        assertThat(updatedOrder.getShippingFee()).isEqualTo(this.updateOrderDTO.shippingFee());
        assertThat(updatedOrder.isWithCoupon()).isEqualTo(this.updateOrderDTO.withCoupon());
        assertThat(updatedOrder.getItems().getFirst().getProductName())
          .isEqualTo(this.updateOrderDTO.products().getFirst().name());
        assertThat(updatedOrder.getItems().getFirst().getFinalPrice().toPlainString())
          .isEqualTo(this.updateOrderDTO.products().getFirst().unitPrice().toPlainString());
      }));
    verify(this.orderGateway, times(1)).findOrderById(order.getId());
  }

  @Test
  @DisplayName("updateOrderFailsByOrderNotFound - Should throw a DataNotFoundException if the order is not found")
  void updateOrderFailsByOrderNotFound() {
    UUID orderId = this.dataMock.getOrdersDomain().getFirst().getId();

    when(this.orderGateway.findOrderById(orderId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.updateOrderUseCase.execute(orderId, this.updateOrderDTO));

    assertThat(thrown)
      .isExactlyInstanceOf(OrderNotFoundException.class)
      .hasMessage("Pedido de id '%s' não encontrado", orderId);

    verify(this.orderGateway, times(1)).findOrderById(orderId);
    verify(this.orderGateway, never()).updateOrder(any(Order.class));
  }
}