package com.felipe.kafka.saga.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.felipe.kafka.saga.BaseSagaTransaction;

import java.util.UUID;

@JsonDeserialize(builder = ShippingTransactionCreateCommand.Builder.class)
public final class ShippingTransactionCreateCommand extends BaseSagaTransaction {
  private final UUID orderId;

  private ShippingTransactionCreateCommand(Builder builder) {
    super(builder.sagaId, builder.transactionId, Command.CREATE);
    this.orderId = builder.orderId;
  }

  public UUID getOrderId() {
    return this.orderId;
  }

  public static Builder startTransaction(UUID sagaId, UUID transactionId) {
    return new Builder(sagaId, transactionId);
  }

  @JsonPOJOBuilder
  public static class Builder {
    private final UUID sagaId;
    private final UUID transactionId;
    private UUID orderId;

    @JsonCreator
    private Builder(@JsonProperty("sagaId") UUID sagaId, @JsonProperty("transactionId") UUID transactionId) {
      this.sagaId = sagaId;
      this.transactionId = transactionId;
    }

    public Builder withOrderId(UUID orderId) {
      this.orderId = orderId;
      return this;
    }

    public ShippingTransactionCreateCommand build() {
      return new ShippingTransactionCreateCommand(this);
    }
  }
}
