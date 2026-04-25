package com.felipe.kafka.saga.replies;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.UUID;

@JsonDeserialize(builder = ShippingTransactionReply.Builder.class)
public final class ShippingTransactionReply extends ReplyTransaction {
  private final String shippingFee;

  private ShippingTransactionReply(Builder builder) {
    super(
      builder.sagaId,
      builder.transactionId,
      builder.orderId,
      builder.command,
      builder.status,
      builder.failureCode,
      builder.failureMessage,
      builder.participant
    );
    this.shippingFee = builder.shippingFee;
  }

  public String getShippingFee() {
    return this.shippingFee;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonPOJOBuilder
  public static class Builder {
    private UUID sagaId;
    private UUID transactionId;
    private UUID orderId;
    private Command command;
    private Status status;
    private FailureCode failureCode;
    private String failureMessage;
    private SagaParticipant participant;
    private String shippingFee;

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

    public Builder withShippingFee(String shippingFee) {
      this.shippingFee = shippingFee;
      return this;
    }

    public Builder success() {
      this.status = Status.SUCCESS;
      return this;
    }

    public Builder failure() {
      this.status = Status.FAILURE;
      return this;
    }

    public ShippingTransactionReply build() {
      return new ShippingTransactionReply(this);
    }
  }
}
