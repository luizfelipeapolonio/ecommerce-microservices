package com.felipe.ecommerce_mail_service.persistence.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "mail_to")
public class MailTo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "order_id", nullable = false)
  private UUID orderId;

  @Column(name = "order_price", nullable = false)
  private String orderPrice;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String username;

  @Column(name = "shipping_fee", nullable = false, length = 100)
  private String shippingFee;

  @Column(name = "coupon_value", length = 100)
  private String couponValue;

  @Column(name = "tracking_code")
  private String trackingCode;

  @Column(name = "invoice_url")
  private String invoiceUrl;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "mailTo", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<OrderItem> orderItems = new ArrayList<>();

  @OneToMany(mappedBy = "mailTo", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<EmailMessage> emailMessages = new ArrayList<>();

  public MailTo() {
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UUID getOrderId() {
    return this.orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public String getOrderPrice() {
    return this.orderPrice;
  }

  public void setOrderPrice(String orderPrice) {
    this.orderPrice = orderPrice;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getShippingFee() {
    return this.shippingFee;
  }

  public void setShippingFee(String shippingFee) {
    this.shippingFee = shippingFee;
  }

  public String getCouponValue() {
    return this.couponValue;
  }

  public void setCouponValue(String couponValue) {
    this.couponValue = couponValue;
  }

  public String getTrackingCode() {
    return this.trackingCode;
  }

  public void setTrackingCode(String trackingCode) {
    this.trackingCode = trackingCode;
  }

  public String getInvoiceUrl() {
    return this.invoiceUrl;
  }

  public void setInvoiceUrl(String invoiceUrl) {
    this.invoiceUrl = invoiceUrl;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public List<OrderItem> getOrderItems() {
    return this.orderItems;
  }

  public void addOrderItem(OrderItem item) {
    item.setMailTo(this);
    this.orderItems.add(item);
  }

  public List<EmailMessage> getEmailMessages() {
    return this.emailMessages;
  }

  public void addEmailMessage(EmailMessage emailMessage) {
    emailMessage.setMailTo(this);
    this.emailMessages.add(emailMessage);
  }
}
