package com.felipe.ecommerce_order_service.infrastructure.saga.event;

import com.felipe.kafka.saga.replies.ReplyTransaction;

import java.util.UUID;

public record ParticipantSucceeded(ReplyTransaction.SagaParticipant participant, UUID orderId) implements SagaEvent {
}
