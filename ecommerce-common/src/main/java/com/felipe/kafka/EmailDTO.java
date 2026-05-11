package com.felipe.kafka;

import java.util.UUID;

public class EmailDTO {
  private Subject subject;
  private String emailTo;
  private String username;
  private UUID orderId;
  private String orderPrice;
  private String trackingCode;
  private String invoiceUrl;

  public enum Subject {
    APPROVED_PAYMENT("Pagamento aprovado"),
    CREATED_INVOICE("Nota fiscal emitida"),
    PREPARING_SHIPMENT("Preparando o pedido"),
    SHIPMENT_OUT_FOR_DELIVERY("Pedido saiu para entrega"),
    DELIVERED_SHIPMENT("Pedido entregue");

    private final String text;

    Subject(String text) {
      this.text = text;
    }

    public String text() {
      return this.text;
    }
  }

  public EmailDTO() {
  }

  public Subject getSubject() {
    return this.subject;
  }

  public EmailDTO setSubject(Subject subject) {
    this.subject = subject;
    return this;
  }

  public String getEmailTo() {
    return this.emailTo;
  }

  public EmailDTO setEmailTo(String emailTo) {
    this.emailTo = emailTo;
    return this;
  }

  public String getUsername() {
    return this.username;
  }

  public EmailDTO setUsername(String username) {
    this.username = username;
    return this;
  }

  public UUID getOrderId() {
    return this.orderId;
  }

  public EmailDTO setOrderId(UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  public String getOrderPrice() {
    return this.orderPrice;
  }

  public EmailDTO setOrderPrice(String orderPrice) {
    this.orderPrice = orderPrice;
    return this;
  }

  public String getTrackingCode() {
    return this.trackingCode;
  }

  public EmailDTO setTrackingCode(String trackingCode) {
    this.trackingCode = trackingCode;
    return this;
  }

  public String getInvoiceUrl() {
    return this.invoiceUrl;
  }

  public EmailDTO setInvoiceUrl(String invoiceUrl) {
    this.invoiceUrl = invoiceUrl;
    return this;
  }
}
