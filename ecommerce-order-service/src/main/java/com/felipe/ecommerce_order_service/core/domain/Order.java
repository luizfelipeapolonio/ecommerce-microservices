package com.felipe.ecommerce_order_service.core.domain;

import com.felipe.ecommerce_order_service.core.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Order {
  private UUID id;
  private BigDecimal orderPrice;
  private boolean withCoupon;
  private UUID couponId;
  private String checkoutUrl;
  private String invoiceUrl;
  private UUID customerId;
  private String status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<OrderItem> items = new ArrayList<>();

  public Order() {
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Order id(UUID id) {
    this.id = id;
    return this;
  }

  public BigDecimal getOrderPrice() {
    return this.orderPrice;
  }

  public void setOrderPrice(BigDecimal orderPrice) {
    this.orderPrice = orderPrice;
  }

  public Order orderPrice(BigDecimal orderPrice) {
    this.orderPrice = orderPrice;
    return this;
  }

  public boolean isWithCoupon() {
    return this.withCoupon;
  }

  public void setWithCoupon(boolean withCoupon) {
    this.withCoupon = withCoupon;
  }

  public Order withCoupon(boolean withCoupon) {
    this.withCoupon = withCoupon;
    return this;
  }

  public UUID getCouponId() {
    return this.couponId;
  }

  public void setCouponId(UUID couponId) {
    this.couponId = couponId;
  }

  public Order couponId(UUID couponId) {
    this.couponId = couponId;
    return this;
  }

  public String getCheckoutUrl() {
    return this.checkoutUrl;
  }

  public void setCheckoutUrl(String checkoutUrl) {
    this.checkoutUrl = checkoutUrl;
  }

  public Order checkoutUrl(String checkoutUrl) {
    this.checkoutUrl = checkoutUrl;
    return this;
  }

  public String getInvoiceUrl() {
    return this.invoiceUrl;
  }

  public void setInvoiceUrl(String invoiceUrl) {
    this.invoiceUrl = invoiceUrl;
  }

  public Order invoiceUrl(String invoiceUrl) {
    this.invoiceUrl = invoiceUrl;
    return this;
  }

  public UUID getCustomerId() {
    return this.customerId;
  }

  public void setCustomerId(UUID customerId) {
    this.customerId = customerId;
  }

  public Order customerId(UUID customerId) {
    this.customerId = customerId;
    return this;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status.getText();
  }

  public Order status(OrderStatus status) {
    this.status = status.getText();
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Order createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Order updatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  public List<OrderItem> getItems() {
    return this.items;
  }

  public void setItems(List<OrderItem> items) {
    this.items = items;
  }

  public void addItem(OrderItem item) {
    item.setOrder(this);
    this.items.add(item);
  }

  public Map<UUID, OrderItem> getItemsMap() {
    return this.items.stream()
      .collect(Collectors.toMap(
        OrderItem::getProductId,
        Function.identity())
      );
  }
}
