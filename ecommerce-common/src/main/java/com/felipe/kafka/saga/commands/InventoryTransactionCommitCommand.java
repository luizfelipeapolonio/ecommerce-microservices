package com.felipe.kafka.saga.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.felipe.kafka.saga.BaseSagaTransaction;

import java.util.UUID;

@JsonDeserialize(builder = InventoryTransactionCommitCommand.Builder.class)
public final class InventoryTransactionCommitCommand extends BaseSagaTransaction {
  private final UUID orderId;

  private InventoryTransactionCommitCommand(Builder builder) {
    super(builder.sagaId, builder.transactionId, Command.COMMIT);
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

    public InventoryTransactionCommitCommand build() {
      return new InventoryTransactionCommitCommand(this);
    }
  }
}
