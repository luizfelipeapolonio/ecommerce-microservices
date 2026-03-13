package com.felipe.kafka.saga.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.felipe.kafka.saga.BaseSagaTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonDeserialize(builder = InventoryTransactionCreateCommand.Builder.class)
public final class InventoryTransactionCreateCommand extends BaseSagaTransaction {
  private final UUID orderId;
  private final List<ProductData> products;

  private InventoryTransactionCreateCommand(Builder builder) {
    super(builder.sagaId, builder.transactionId, Command.CREATE);
    this.orderId = builder.orderId;
    this.products = builder.products;
  }

  public UUID getOrderId() {
    return this.orderId;
  }

  public List<ProductData> getProducts() {
    return this.products;
  }

  public static Builder startTransaction(UUID sagaId, UUID transactionId) {
    return new Builder(sagaId, transactionId);
  }

  public static Builder mutate(InventoryTransactionCreateCommand inventoryCommand) {
    return new Builder(inventoryCommand);
  }

  public record ProductData(UUID id, long quantity) {}

  @JsonPOJOBuilder
  public static class Builder {
    private final UUID sagaId;
    private final UUID transactionId;
    private UUID orderId;
    private List<ProductData> products = new ArrayList<>();

    @JsonCreator
    private Builder(@JsonProperty("sagaId") UUID sagaId, @JsonProperty("transactionId") UUID transactionId) {
      this.sagaId = sagaId;
      this.transactionId = transactionId;
    }

    public Builder(InventoryTransactionCreateCommand inventoryCommand) {
      this.sagaId = inventoryCommand.getSagaId();
      this.transactionId = inventoryCommand.getTransactionId();
      this.orderId = inventoryCommand.getOrderId();
      this.products = inventoryCommand.getProducts();
    }

    public Builder withOrderId(UUID orderId) {
      this.orderId = orderId;
      return this;
    }

    public Builder withProducts(List<ProductData> products) {
      this.products = products;
      return this;
    }

    public InventoryTransactionCreateCommand build() {
      return new InventoryTransactionCreateCommand(this);
    }
  }
}
