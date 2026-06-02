package com.felipe.ecommerce_order_service.testutils;

import com.felipe.ecommerce_order_service.core.application.dtos.AddressDTO;
import com.felipe.ecommerce_order_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.core.domain.OrderItem;
import com.felipe.ecommerce_order_service.core.domain.enums.OrderStatus;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.OrderEntity;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.OrderItemEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataMock {
  private final CustomerProfileDTO customerProfileDTO;
  private final List<Order> ordersDomain = new ArrayList<>();
  private final List<OrderItem> orderItemsDomain = new ArrayList<>();
  private final List<OrderEntity> ordersEntity = new ArrayList<>();
  private final List<OrderItemEntity> orderItemsEntity = new ArrayList<>();

  public DataMock() {
    this.customerProfileDTO = new CustomerProfileDTO(
      "1c6362db-13da-4419-8dbb-b6366e01ba9f",
      "test@email.com",
      "User1",
      "User",
      "Test",
      "2026-05-29T18:13:00.657570300",
      "2026-05-29T18:13:00.657570300",
      new AddressDTO(
        "Some street",
        "12",
        "house",
        "Somewhere",
        "12345-678",
        "Some city",
        "Some state",
        "Some country"
      )
    );
    createOrdersDomain();
    createOrderItemsDomain();
    createOrdersEntity();
    createOrderItemsEntity();
  }

  public CustomerProfileDTO getCustomerProfileDTO() { return this.customerProfileDTO; }
  public List<Order> getOrdersDomain() { return this.ordersDomain; }
  public List<OrderItem> getOrderItemsDomain() { return this.orderItemsDomain; }
  public List<OrderEntity> getOrdersEntity() { return this.ordersEntity; }
  public List<OrderItemEntity> getOrderItemsEntity() { return this.orderItemsEntity; }

  private void createOrdersDomain() {
    Order order1 = new Order()
      .id(UUID.fromString("95480447-11bd-4b55-a0af-821339812588"))
      .orderPrice(new BigDecimal("100.00"))
      .withCoupon(false)
      .couponCode(null)
      .shippingFee(new BigDecimal("20.00"))
      .checkoutUrl("https://checkoutUrl.com")
      .invoiceUrl("https://invoiceUrl.com")
      .customerId(UUID.fromString(this.customerProfileDTO.id()))
      .status(OrderStatus.PENDING)
      .createdAt(LocalDateTime.parse("2026-05-29T18:13:00.657570300"))
      .updatedAt(LocalDateTime.parse("2026-05-29T18:13:00.657570300"));

    this.ordersDomain.add(order1);
  }

  private void createOrderItemsDomain() {
    OrderItem orderItem1 = new OrderItem()
      .id(1L)
      .productName("Item 1")
      .productId(UUID.fromString("0008f5ca-70ab-48c6-a9c5-976b0191bbf4"))
      .finalPrice(new BigDecimal("150.00"))
      .quantity(1)
      .addedAt(LocalDateTime.parse("2026-05-29T18:13:00.657570300"));

    this.orderItemsDomain.add(orderItem1);
  }

  private void createOrdersEntity() {
    OrderEntity order1 = new OrderEntity()
      .id(UUID.fromString("95480447-11bd-4b55-a0af-821339812588"))
      .orderPrice(new BigDecimal("100.00"))
      .withCoupon(false)
      .couponCode(null)
      .shippingFee(new BigDecimal("20.00"))
      .checkoutUrl("https://checkoutUrl.com")
      .invoiceUrl("https://invoiceUrl.com")
      .customerId(UUID.fromString(this.customerProfileDTO.id()))
      .status(OrderStatus.PENDING)
      .createdAt(LocalDateTime.parse("2026-05-29T18:13:00.657570300"))
      .updatedAt(LocalDateTime.parse("2026-05-29T18:13:00.657570300"));

    this.ordersEntity.add(order1);
  }

  private void createOrderItemsEntity() {
    OrderItemEntity orderItem1 = new OrderItemEntity()
      .id(1L)
      .productName("Item 1")
      .productId(UUID.fromString("0008f5ca-70ab-48c6-a9c5-976b0191bbf4"))
      .finalPrice(new BigDecimal("150.00"))
      .quantity(1)
      .addedAt(LocalDateTime.parse("2026-05-29T18:13:00.657570300"));

    this.orderItemsEntity.add(orderItem1);
  }
}
