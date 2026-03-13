package com.felipe.kafka.saga.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.felipe.kafka.saga.BaseSagaTransaction;

import java.util.List;
import java.util.UUID;

@JsonDeserialize(builder = PaymentTransactionCreateCommand.Builder.class)
public final class PaymentTransactionCreateCommand extends BaseSagaTransaction {
  private final UUID orderId;
  private final String orderAmount;
  private final List<ProductData> products;
  private final CustomerData customer;

  private PaymentTransactionCreateCommand(Builder builder) {
    super(builder.sagaId, builder.transactionId, Command.CREATE);
    this.orderId = builder.orderId;
    this.orderAmount = builder.orderAmount;
    this.products = builder.products;
    this.customer = builder.customer;
  }

  public UUID getOrderId() {
    return this.orderId;
  }

  public String getOrderAmount() {
    return this.orderAmount;
  }

  public List<ProductData> getProducts() {
    return this.products;
  }

  public CustomerData getCustomer() {
    return this.customer;
  }

  public static Builder builder(UUID sagaId, UUID transactionId) {
    return new Builder(sagaId, transactionId);
  }

  public record ProductData(String name, long quantity, String unitPrice, String discountType, String discountValue) {}

  public record CustomerData(UUID id, String name, String email, CustomerAddress address) {}

  public record CustomerAddress(String street,
                                String number,
                                String complement,
                                String district,
                                String zipcode,
                                String city,
                                String state,
                                String country) {}

  @JsonPOJOBuilder
  public static class Builder {
    private final UUID sagaId;
    private final UUID transactionId;
    private UUID orderId;
    private String orderAmount;
    private List<ProductData> products;
    private CustomerData customer;

    @JsonCreator
    private Builder(@JsonProperty("sagaId") UUID sagaId, @JsonProperty("transactionId") UUID transactionId) {
      this.sagaId = sagaId;
      this.transactionId = transactionId;
    }

    public Builder withOrderId(UUID orderId) {
      this.orderId = orderId;
      return this;
    }

    public Builder withOrderAmount(String orderAmount) {
      this.orderAmount = orderAmount;
      return this;
    }

    public Builder withProducts(List<ProductData> products) {
      this.products = products;
      return this;
    }

    public Builder withCustomer(CustomerData customer) {
      this.customer = customer;
      return this;
    }

    public PaymentTransactionCreateCommand build() {
      return new PaymentTransactionCreateCommand(this);
    }
  }
}
