package com.felipe.kafka.saga.replies;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.UUID;

@JsonDeserialize(builder = DiscountTransactionReply.Builder.class)
public final class DiscountTransactionReply extends ReplyTransaction {
  private final String couponCode;
  private final String discountType;
  private final String discountValue;

  private DiscountTransactionReply(Builder builder) {
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
    this.couponCode = builder.couponCode;
    this.discountType = builder.discountType;
    this.discountValue = builder.discountValue;
  }

  public String getCouponCode() {
    return this.couponCode;
  }

  public String getDiscountType() {
    return this.discountType;
  }

  public String getDiscountValue() {
    return this.discountValue;
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
    private String couponCode;
    private String discountType;
    private String discountValue;

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

    public Builder withCouponCode(String couponCode) {
      this.couponCode = couponCode;
      return this;
    }

    public Builder withDiscountType(String discountType) {
      this.discountType = discountType;
      return this;
    }

    public Builder withDiscountValue(String discountValue) {
      this.discountValue = discountValue;
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

    public DiscountTransactionReply build() {
      return new DiscountTransactionReply(this);
    }
  }
}
