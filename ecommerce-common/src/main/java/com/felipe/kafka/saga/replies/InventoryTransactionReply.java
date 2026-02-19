package com.felipe.kafka.saga.replies;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.felipe.kafka.saga.BaseSagaTransaction;
import com.felipe.kafka.saga.enums.FailureCode;
import com.felipe.kafka.saga.enums.SagaParticipant;

import java.util.UUID;

@JsonDeserialize(builder = InventoryTransactionReply.Builder.class)
public final class InventoryTransactionReply extends BaseSagaTransaction {
  private final UUID productId;
  private final UUID orderId;
  private final Status status;
  private final FailureCode failureCode;
  private final String failureMessage;
  private final SagaParticipant participant;

  public enum Status {
    SUCCESS,
    FAILURE
  }

  private InventoryTransactionReply(Builder builder) {
    super(builder.sagaId, builder.transactionId, builder.command);
    this.productId = builder.productId;
    this.orderId = builder.orderId;
    this.status = builder.status;
    this.failureCode = builder.failureCode;
    this.failureMessage = builder.failureMessage;
    this.participant = builder.participant;
  }

  public UUID getProductId() {
    return this.productId;
  }

  public UUID getOrderId() {
    return this.orderId;
  }

  public Status getStatus() {
    return this.status;
  }

  public FailureCode getFailureCode() {
    return this.failureCode;
  }

  public String getFailureMessage() {
    return this.failureMessage;
  }

  public SagaParticipant getParticipant() {
    return this.participant;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonPOJOBuilder
  public static class Builder {
    private UUID sagaId;
    private UUID transactionId;
    private UUID productId;
    private UUID orderId;
    private Command command;
    private Status status;
    private FailureCode failureCode;
    private String failureMessage;
    private SagaParticipant participant;

    private Builder() {
    }

    public Builder withSagaId(UUID sagaId) {
      this.sagaId = sagaId;
      return this;
    }

    public Builder withTransactionId(UUID transactionId) {
      this.transactionId = transactionId;
      return this;
    }

    public Builder withProductId(UUID productId) {
      this.productId = productId;
      return this;
    }

    public Builder withOrderId(UUID orderId) {
      this.orderId = orderId;
      return this;
    }

    public Builder withCommand(Command command) {
      this.command = command;
      return this;
    }

    // For jackson deserialization purposes only
    // Use success or fail methods to assign a status
    // in a more direct and readable way
    Builder withStatus(Status status) {
      this.status = status;
      return this;
    }

    public Builder withFailureCode(FailureCode failureCode) {
      this.failureCode = failureCode;
      return this;
    }

    public Builder withFailureMessage(String failureMessage) {
      this.failureMessage = failureMessage;
      return this;
    }

    public Builder withParticipant(SagaParticipant participant) {
      this.participant = participant;
      return this;
    }

    public Builder success() {
      this.status = Status.SUCCESS;
      return this;
    }

    public Builder fail() {
      this.status = Status.FAILURE;
      return this;
    }

    public InventoryTransactionReply build() {
      return new InventoryTransactionReply(this);
    }
  }
}
