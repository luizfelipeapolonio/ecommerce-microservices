package com.felipe.kafka.saga.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.felipe.kafka.saga.BaseSagaTransaction;

import java.util.UUID;

@JsonDeserialize(builder = DiscountTransactionCancelCommand.Builder.class)
public final class DiscountTransactionCancelCommand extends BaseSagaTransaction {
  private final String couponCode;
  private final UUID orderId;
  private final UUID customerId;

  private DiscountTransactionCancelCommand(Builder builder) {
    super(builder.sagaId, builder.transactionId, Command.CANCEL);
    this.couponCode = builder.couponCode;
    this.orderId = builder.orderId;
    this.customerId = builder.customerId;
  }

  public String getCouponCode() {
    return this.couponCode;
  }

  public UUID getOrderId() {
    return this.orderId;
  }

  public UUID getCustomerId() {
    return this.customerId;
  }

  public static Builder startTransaction(UUID sagaId, UUID transactionId) {
    return new Builder(sagaId, transactionId);
  }

  @JsonPOJOBuilder
  public static class Builder {
    private final UUID sagaId;
    private final UUID transactionId;
    private String couponCode;
    private UUID orderId;
    private UUID customerId;

    @JsonCreator
    private Builder(@JsonProperty("sagaId") UUID sagaId, @JsonProperty("transactionId") UUID transactionId) {
      this.sagaId = sagaId;
      this.transactionId = transactionId;
    }

    public Builder withCouponCode(String couponCode) {
      this.couponCode = couponCode;
      return this;
    }

    public Builder withOrderId(UUID orderId) {
      this.orderId = orderId;
      return this;
    }

    public Builder withCustomerId(UUID customerId) {
      this.customerId = customerId;
      return this;
    }

    public DiscountTransactionCancelCommand build() {
      return new DiscountTransactionCancelCommand(this);
    }
  }
}
