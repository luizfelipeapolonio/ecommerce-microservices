package com.felipe.kafka.saga.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.felipe.kafka.saga.BaseSagaTransaction;

import java.util.UUID;

@JsonDeserialize(builder = InventoryTransactionCreateCommand.Builder.class)
public final class InventoryTransactionCreateCommand extends BaseSagaTransaction {
  private final UUID productId;
  private final UUID orderId;
  private final int productQuantity;

  private InventoryTransactionCreateCommand(Builder builder) {
    super(builder.sagaId, builder.transactionId, Command.CREATE);
    this.productId = builder.productId;
    this.orderId = builder.orderId;
    this.productQuantity = builder.productQuantity;
  }

  public UUID getProductId() {
    return this.productId;
  }

  public UUID getOrderId() {
    return this.orderId;
  }

  public int getProductQuantity() {
    return this.productQuantity;
  }

  public static Builder startTransaction(UUID sagaId, UUID transactionId) {
    return new Builder(sagaId,transactionId);
  }

  public static Builder mutate(InventoryTransactionCreateCommand inventoryCommand) {
    return new Builder(inventoryCommand);
  }

  @JsonPOJOBuilder
  public static class Builder {
    private final UUID sagaId;
    private final UUID transactionId;
    private UUID productId;
    private UUID orderId;
    private int productQuantity;

    @JsonCreator
    private Builder(@JsonProperty("sagaId") UUID sagaId, @JsonProperty("transactionId") UUID transactionId) {
      this.sagaId = sagaId;
      this.transactionId = transactionId;
    }

    public Builder(InventoryTransactionCreateCommand inventoryCommand) {
      this.sagaId = inventoryCommand.getSagaId();
      this.transactionId = inventoryCommand.getTransactionId();
      this.productId = inventoryCommand.getProductId();
      this.orderId = inventoryCommand.getOrderId();
      this.productQuantity = inventoryCommand.getProductQuantity();
    }

    public Builder withProductId(UUID productId) {
      this.productId = productId;
      return this;
    }

    public Builder withOrderId(UUID orderId) {
      this.orderId = orderId;
      return this;
    }

    public Builder withProductQuantity(int productQuantity) {
      this.productQuantity = productQuantity;
      return this;
    }

    public InventoryTransactionCreateCommand build() {
      return new InventoryTransactionCreateCommand(this);
    }
  }
}
