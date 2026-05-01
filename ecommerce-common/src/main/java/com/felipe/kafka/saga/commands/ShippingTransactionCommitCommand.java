package com.felipe.kafka.saga.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.felipe.kafka.saga.BaseSagaTransaction;

import java.util.UUID;

@JsonDeserialize(builder = ShippingTransactionCommitCommand.Builder.class)
public final class ShippingTransactionCommitCommand extends BaseSagaTransaction {
  private final UUID orderId;
  private final ShipmentAddress shipmentAddress;

  private ShippingTransactionCommitCommand(Builder builder) {
    super(builder.sagaId, builder.transactionId, Command.COMMIT);
    this.orderId = builder.orderId;
    this.shipmentAddress = builder.shipmentAddress;
  }

  public UUID getOrderId() {
    return this.orderId;
  }

  public ShipmentAddress getShipmentAddress() {
    return this.shipmentAddress;
  }

  public static Builder startTransaction(UUID sagaId, UUID transactionId) {
    return new Builder(sagaId, transactionId);
  }

  public record ShipmentAddress(
    String street,
    String number,
    String complement,
    String district,
    String zipcode,
    String city,
    String state,
    String country
  ) {}

  @JsonPOJOBuilder
  public static class Builder {
    private final UUID sagaId;
    private final UUID transactionId;
    private UUID orderId;
    private ShipmentAddress shipmentAddress;

    @JsonCreator
    private Builder(@JsonProperty("sagaId") UUID sagaId, @JsonProperty("transactionId") UUID transactionId) {
      this.sagaId = sagaId;
      this.transactionId = transactionId;
    }

    public Builder withOrderId(UUID orderId) {
      this.orderId = orderId;
      return this;
    }

    public Builder withShipmentAddress(ShipmentAddress shipmentAddress) {
      this.shipmentAddress = shipmentAddress;
      return this;
    }

    public ShippingTransactionCommitCommand build() {
      return new ShippingTransactionCommitCommand(this);
    }
  }
}
