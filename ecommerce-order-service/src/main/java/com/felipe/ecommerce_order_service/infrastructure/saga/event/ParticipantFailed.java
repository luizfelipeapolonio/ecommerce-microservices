package com.felipe.ecommerce_order_service.infrastructure.saga.event;

import com.felipe.kafka.saga.replies.ReplyTransaction;

import java.util.UUID;

public record ParticipantFailed(
  ReplyTransaction.SagaParticipant participant,
  ReplyTransaction.FailureCode failureCode,
  String failureMessage,
  UUID orderId,
  UUID productId
) implements SagaEvent {}
