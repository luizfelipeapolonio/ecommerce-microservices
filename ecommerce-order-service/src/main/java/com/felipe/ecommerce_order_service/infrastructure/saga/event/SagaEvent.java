package com.felipe.ecommerce_order_service.infrastructure.saga.event;

import com.felipe.kafka.saga.replies.InventoryTransactionReply;
import com.felipe.kafka.saga.replies.ReplyTransaction;

import java.util.UUID;

public sealed interface SagaEvent permits ParticipantSucceeded, ParticipantFailed {
  ReplyTransaction.SagaParticipant participant();
  UUID orderId();

  static SagaEvent mapReplyToEvent(ReplyTransaction reply) {
    UUID orderId = null;
    UUID productId = null;

    if (reply instanceof InventoryTransactionReply inventoryReply) {
      orderId = inventoryReply.getOrderId();
      productId = inventoryReply.getProduct().getId();
    }

    return switch (reply.getStatus()) {
      case SUCCESS -> new ParticipantSucceeded(reply.getParticipant(), orderId);
      case FAILURE -> new ParticipantFailed(reply.getParticipant(), reply.getFailureCode(), reply.getFailureMessage(), orderId, productId);
    };
  }
}
