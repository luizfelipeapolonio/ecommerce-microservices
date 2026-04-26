package com.felipe.ecommerce_order_service.infrastructure.persistence.entities;

import com.felipe.ecommerce_order_service.core.domain.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "order_price", nullable = false)
  private BigDecimal orderPrice;

  @Column(name = "with_coupon", nullable = false)
  private boolean withCoupon = false;

  @Column(name = "coupon_code", length = 150)
  private String couponCode;

  @Column(name = "coupon_value")
  private BigDecimal couponValue;

  @Column(name = "shipping_fee")
  private BigDecimal shippingFee;

  @Column(name = "checkout_url")
  private String checkoutUrl;

  @Column(name = "invoice_url")
  private String invoiceUrl;

  @Column(name = "customer_id", nullable = false)
  private UUID customerId;

  @Column(nullable = false, length = 20)
  private String status;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItemEntity> items = new ArrayList<>();

  public OrderEntity() {
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public OrderEntity id(UUID id) {
    this.id = id;
    return this;
  }

  public BigDecimal getOrderPrice() {
    return this.orderPrice;
  }

  public void setOrderPrice(BigDecimal orderPrice) {
    this.orderPrice = orderPrice;
  }

  public OrderEntity orderPrice(BigDecimal orderPrice) {
    this.orderPrice = orderPrice;
    return this;
  }

  public boolean isWithCoupon() {
    return this.withCoupon;
  }

  public void setWithCoupon(boolean withCoupon) {
    this.withCoupon = withCoupon;
  }

  public OrderEntity withCoupon(boolean withCoupon) {
    this.withCoupon = withCoupon;
    return this;
  }

  public String getCouponCode() {
    return this.couponCode;
  }

  public void setCouponCode(String couponCode) {
    this.couponCode = couponCode;
  }

  public OrderEntity couponCode(String couponCode) {
    this.couponCode = couponCode;
    return this;
  }

  public BigDecimal getCouponValue() {
    return this.couponValue;
  }

  public void setCouponValue(BigDecimal couponValue) {
    this.couponValue = couponValue;
  }

  public OrderEntity couponValue(BigDecimal couponValue) {
    this.couponValue = couponValue;
    return this;
  }

  public BigDecimal getShippingFee() {
    return this.shippingFee;
  }

  public void setShippingFee(BigDecimal shippingFee) {
    this.shippingFee = shippingFee;
  }

  public OrderEntity shippingFee(BigDecimal shippingFee) {
    this.shippingFee = shippingFee;
    return this;
  }

  public String getCheckoutUrl() {
    return this.checkoutUrl;
  }

  public void setCheckoutUrl(String checkoutUrl) {
    this.checkoutUrl = checkoutUrl;
  }

  public OrderEntity checkoutUrl(String checkoutUrl) {
    this.checkoutUrl = checkoutUrl;
    return this;
  }

  public String getInvoiceUrl() {
    return this.invoiceUrl;
  }

  public void setInvoiceUrl(String invoiceUrl) {
    this.invoiceUrl = invoiceUrl;
  }

  public OrderEntity invoiceUrl(String invoiceUrl) {
    this.invoiceUrl = invoiceUrl;
    return this;
  }

  public UUID getCustomerId() {
    return this.customerId;
  }

  public void setCustomerId(UUID customerId) {
    this.customerId = customerId;
  }

  public OrderEntity customerId(UUID customerId) {
    this.customerId = customerId;
    return this;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status.getText();
  }

  public OrderEntity status(OrderStatus status) {
    this.status = status.getText();
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public OrderEntity createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public OrderEntity updatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  public List<OrderItemEntity> getItems() {
    return this.items;
  }

  public void setItems(List<OrderItemEntity> items) {
    this.items = items;
  }

  public void addItem(OrderItemEntity item) {
    item.setOrder(this);
    this.items.add(item);
  }
}
