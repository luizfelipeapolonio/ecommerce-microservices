package com.felipe.ecommerce_order_service.infrastructure.mappers;

import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.core.domain.OrderItem;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.OrderEntity;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.OrderItemEntity;
import com.felipe.ecommerce_order_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderEntityMapperTest {

  @Spy
  private OrderEntityMapper orderEntityMapper;

  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("convertEntityToDomain - Should successfully convert an OrderEntity to Order")
  void convertEntityToDomain() {
    OrderEntity orderEntity = this.dataMock.getOrdersEntity().getFirst();
    OrderItemEntity item = this.dataMock.getOrderItemsEntity().getFirst();
    orderEntity.addItem(item);

    Order convertedOrder = this.orderEntityMapper.toDomain(orderEntity);

    assertThat(convertedOrder.getId()).isEqualTo(orderEntity.getId());
    assertThat(convertedOrder.getCustomerId()).isEqualTo(orderEntity.getCustomerId());
    assertThat(convertedOrder.getOrderPrice()).isEqualTo(orderEntity.getOrderPrice());
    assertThat(convertedOrder.getStatus()).isEqualTo(orderEntity.getStatus());
    assertThat(convertedOrder.isWithCoupon()).isEqualTo(orderEntity.isWithCoupon());
    assertThat(convertedOrder.getCouponCode()).isEqualTo(orderEntity.getCouponCode());
    assertThat(convertedOrder.getCouponValue()).isEqualTo(orderEntity.getCouponValue());
    assertThat(convertedOrder.getShippingFee()).isEqualTo(orderEntity.getShippingFee());
    assertThat(convertedOrder.getCheckoutUrl()).isEqualTo(orderEntity.getCheckoutUrl());
    assertThat(convertedOrder.getInvoiceUrl()).isEqualTo(orderEntity.getInvoiceUrl());
    assertThat(convertedOrder.getCreatedAt()).isEqualTo(orderEntity.getCreatedAt());
    assertThat(convertedOrder.getUpdatedAt()).isEqualTo(orderEntity.getUpdatedAt());
    assertThat(convertedOrder.getItems().getFirst().getId()).isEqualTo(orderEntity.getItems().getFirst().getId());
    assertThat(convertedOrder.getItems().getFirst().getProductId()).isEqualTo(orderEntity.getItems().getFirst().getProductId());
    assertThat(convertedOrder.getItems().getFirst().getProductName()).isEqualTo(orderEntity.getItems().getFirst().getProductName());
    assertThat(convertedOrder.getItems().getFirst().getQuantity()).isEqualTo(orderEntity.getItems().getFirst().getQuantity());
    assertThat(convertedOrder.getItems().getFirst().getFinalPrice()).isEqualTo(orderEntity.getItems().getFirst().getFinalPrice());
    assertThat(convertedOrder.getItems().getFirst().getAddedAt()).isEqualTo(orderEntity.getItems().getFirst().getAddedAt());

    verify(this.orderEntityMapper, times(1)).toDomain(orderEntity);
  }

  @Test
  @DisplayName("convertDomainToEntity - Should successfully convert an Order to OrderEntity")
  void convertDomainToEntity() {
    Order orderDomain = this.dataMock.getOrdersDomain().getFirst();
    OrderItem item = this.dataMock.getOrderItemsDomain().getFirst();
    orderDomain.addItem(item);

    OrderEntity convertedOrder = this.orderEntityMapper.toEntity(orderDomain);

    assertThat(convertedOrder.getId()).isEqualTo(orderDomain.getId());
    assertThat(convertedOrder.getCustomerId()).isEqualTo(orderDomain.getCustomerId());
    assertThat(convertedOrder.getOrderPrice()).isEqualTo(orderDomain.getOrderPrice());
    assertThat(convertedOrder.getStatus()).isEqualTo(orderDomain.getStatus());
    assertThat(convertedOrder.isWithCoupon()).isEqualTo(orderDomain.isWithCoupon());
    assertThat(convertedOrder.getCouponCode()).isEqualTo(orderDomain.getCouponCode());
    assertThat(convertedOrder.getCouponValue()).isEqualTo(orderDomain.getCouponValue());
    assertThat(convertedOrder.getShippingFee()).isEqualTo(orderDomain.getShippingFee());
    assertThat(convertedOrder.getCheckoutUrl()).isEqualTo(orderDomain.getCheckoutUrl());
    assertThat(convertedOrder.getInvoiceUrl()).isEqualTo(orderDomain.getInvoiceUrl());
    assertThat(convertedOrder.getCreatedAt()).isEqualTo(orderDomain.getCreatedAt());
    assertThat(convertedOrder.getUpdatedAt()).isEqualTo(orderDomain.getUpdatedAt());
    assertThat(convertedOrder.getItems().getFirst().getId()).isEqualTo(orderDomain.getItems().getFirst().getId());
    assertThat(convertedOrder.getItems().getFirst().getProductId()).isEqualTo(orderDomain.getItems().getFirst().getProductId());
    assertThat(convertedOrder.getItems().getFirst().getProductName()).isEqualTo(orderDomain.getItems().getFirst().getProductName());
    assertThat(convertedOrder.getItems().getFirst().getQuantity()).isEqualTo(orderDomain.getItems().getFirst().getQuantity());
    assertThat(convertedOrder.getItems().getFirst().getFinalPrice()).isEqualTo(orderDomain.getItems().getFirst().getFinalPrice());
    assertThat(convertedOrder.getItems().getFirst().getAddedAt()).isEqualTo(orderDomain.getItems().getFirst().getAddedAt());

    verify(this.orderEntityMapper, times(1)).toEntity(orderDomain);
  }
}