package com.felipe.kafka.saga.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.felipe.kafka.saga.BaseSagaTransaction;
import com.felipe.kafka.saga.enums.FailureCode;

import java.util.UUID;

@JsonDeserialize(builder = InventoryTransactionCancelCommand.Builder.class)
public final class InventoryTransactionCancelCommand extends BaseSagaTransaction {
  private final UUID productId;
  private final UUID orderId;
  private final FailureCode failureCode;

  private InventoryTransactionCancelCommand(Builder builder) {
    super(builder.sagaId, builder.transactionId, Command.CANCEL);
    this.productId = builder.productId;
    this.orderId = builder.orderId;
    this.failureCode = builder.failureCode;
  }

  public UUID getProductId() {
    return this.productId;
  }

  public UUID getOrderId() {
    return this.orderId;
  }

  public FailureCode getFailureCode() {
    return this.failureCode;
  }

  public static Builder builder(UUID sagaId, UUID transactionId) {
    return new Builder(sagaId, transactionId);
  }

  @JsonPOJOBuilder
  public static class Builder {
    private final UUID sagaId;
    private final UUID transactionId;
    private UUID productId;
    private UUID orderId;
    private FailureCode failureCode;

    @JsonCreator
    private Builder(@JsonProperty("sagaId") UUID sagaId,@JsonProperty("transactionId") UUID transactionId) {
      this.sagaId = sagaId;
      this.transactionId = transactionId;
    }

    public Builder withProductId(UUID productId) {
      this.productId = productId;
      return this;
    }

    public Builder withOrderId(UUID orderId) {
      this.orderId = orderId;
      return this;
    }

    public Builder withFailureCode(FailureCode failureCode) {
      this.failureCode = failureCode;
      return this;
    }

    public InventoryTransactionCancelCommand build() {
      return new InventoryTransactionCancelCommand(this);
    }
  }
}
