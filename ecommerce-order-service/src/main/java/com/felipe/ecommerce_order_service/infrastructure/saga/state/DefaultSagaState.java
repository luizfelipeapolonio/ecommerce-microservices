package com.felipe.ecommerce_order_service.infrastructure.saga.state;

import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.DeleteOrderUseCase;
import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.CancellingTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.InventoryFailedTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.InventorySucceededTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.PaymentSucceedTransaction;
import com.felipe.kafka.saga.replies.InventoryTransactionReply;
import com.felipe.kafka.saga.replies.PaymentTransactionReply;
import com.felipe.kafka.saga.replies.ReplyTransaction;
import org.springframework.kafka.core.KafkaTemplate;

public class DefaultSagaState implements SagaState {
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final DeleteOrderUseCase deleteOrderUseCase;
  private final UpdateOrderUseCase updateOrderUseCase;
  private final CustomerGateway customerGateway;

  public DefaultSagaState(KafkaTemplate<String, Object> kafkaTemplate,
                          DeleteOrderUseCase deleteOrderUseCase,
                          UpdateOrderUseCase updateOrderUseCase,
                          CustomerGateway customerGateway) {
    this.kafkaTemplate = kafkaTemplate;
    this.deleteOrderUseCase = deleteOrderUseCase;
    this.updateOrderUseCase = updateOrderUseCase;
    this.customerGateway = customerGateway;
  }

  @Override
  public SagaTransition handle(OrderSaga saga, ReplyTransaction reply) {
    return switch (saga.getStatus()) {
      case STARTED -> handleStarted(reply);
      // case STARTED -> {
      //   return switch (reply.getParticipant()) {
      //     case INVENTORY -> {
      //       InventoryTransactionReply inventoryReply = (InventoryTransactionReply) reply;
      //       yield handleInventoryStarted(inventoryReply);
      //     };
      //     case SHIPPING -> {
      //       ShippingTransactionReply shippingReply = (ShippingTransactionReply) reply;
      //       yield handleShippingStarted(shippingReply);
      //     };
      //     default -> throw new UnhandledParticipantException();
      //   }
      //}
      case PROCESSING -> handleProcessing(reply);
      case WAITING_FOR_PAYMENT -> null;
      case COMMITING -> null;
      case COMPLETED -> null;
      case FAILED -> null;
      case CANCELLING -> handleCancelling(reply);
    };
  }

  private SagaTransition handleStarted(ReplyTransaction reply) {
    if (reply instanceof InventoryTransactionReply inventoryReply) {
      return switch (inventoryReply.getStatus()) {
        case SUCCESS -> new InventorySucceededTransition(inventoryReply, this.customerGateway, this.kafkaTemplate);
        case FAILURE -> new InventoryFailedTransition(inventoryReply, this.kafkaTemplate);
      };
    }
    return null; // TODO: throw a specific exception
  }

  private SagaTransition handleProcessing(ReplyTransaction reply) {
    if (reply instanceof PaymentTransactionReply paymentReply) {
      return switch (reply.getStatus()) {
        case SUCCESS -> new PaymentSucceedTransaction(paymentReply, this.updateOrderUseCase);
        case FAILURE -> null;
      };
    }
    return null;
  }

  private SagaTransition handleCancelling(ReplyTransaction reply) {
    return new CancellingTransition(reply, this.deleteOrderUseCase);
  }
}
