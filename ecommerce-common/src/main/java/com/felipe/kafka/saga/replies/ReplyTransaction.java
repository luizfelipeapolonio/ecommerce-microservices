package com.felipe.kafka.saga.replies;

import com.felipe.kafka.saga.BaseSagaTransaction;

import java.util.UUID;

public abstract class ReplyTransaction extends BaseSagaTransaction {
  private final UUID orderId;
  private final Status status;
  private final FailureCode failureCode;
  private final String failureMessage;
  private final SagaParticipant participant;

  public enum Status {
    SUCCESS,
    FAILURE
  }

  public enum FailureCode {
    BUSINESS_EXCEPTION,
    INFRASTRUCTURE_EXCEPTION,
    NO_APPLY
  }

  public enum SagaParticipant {
    INVENTORY,
    PAYMENT,
    DISCOUNT,
    SHIPPING
  }

  public ReplyTransaction(UUID sagaId,
                          UUID transactionId,
                          UUID orderId,
                          Command command,
                          Status status,
                          FailureCode failureCode,
                          String failureMessage,
                          SagaParticipant participant) {
    super(sagaId, transactionId, command);
    this.orderId = orderId;
    this.status = status;
    this.failureCode = failureCode;
    this.failureMessage = failureMessage;
    this.participant = participant;
  }

  public Status getStatus() {
    return this.status;
  }

  public UUID getOrderId() {
    return this.orderId;
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
}
