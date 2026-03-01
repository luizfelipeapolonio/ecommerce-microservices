package com.felipe.ecommerce_order_service.infrastructure.saga;

import com.felipe.ecommerce_order_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.DeleteOrderUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaParticipantStatus;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.event.ParticipantFailed;
import com.felipe.ecommerce_order_service.infrastructure.saga.event.ParticipantSucceeded;
import com.felipe.ecommerce_order_service.infrastructure.saga.event.SagaEvent;
import com.felipe.kafka.saga.commands.InventoryTransactionCancelCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;
import java.util.function.Consumer;

public class DefaultSagaStateMachine implements SagaStateMachine {
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final DeleteOrderUseCase deleteOrderUseCase;
  private final CustomerGateway customerGateway;
  private static final Logger logger = LoggerFactory.getLogger(DefaultSagaStateMachine.class);

  public DefaultSagaStateMachine(KafkaTemplate<String, Object> kafkaTemplate, DeleteOrderUseCase deleteOrderUseCase,
                                 CustomerGateway customerGateway) {
    this.kafkaTemplate = kafkaTemplate;
    this.deleteOrderUseCase = deleteOrderUseCase;
    this.customerGateway = customerGateway;
  }

  @Override
  public SagaTransition handle(OrderSaga saga, SagaEvent event) {
    return switch (saga.getStatus()) {
      case STARTED -> handleStarted(saga.getId(), event);
      case PROCESSING -> null;
      case COMMITING -> null;
      case COMPLETED -> null;
      case FAILED -> null;
      case CANCELLING -> handleCancelling(event);
    };
  }

  private SagaTransition handleStarted(UUID sagaId, SagaEvent event) {
    Consumer<OrderSaga> sagaMutation = orderSaga -> {};
    TriggerAction action = () -> {};

    if (event instanceof ParticipantSucceeded success) {
      sagaMutation = orderSaga -> {
        orderSaga.getParticipants().forEach(participant -> {
          if (participant.getName() == success.participant()) {
            participant.setStatus(SagaParticipantStatus.SUCCESS);
          }
        });
        orderSaga.setStatus(SagaStatus.PROCESSING);
      };
      action = () -> {
        CustomerProfileDTO authCustomer = this.customerGateway.getCurrentAuthCustomer();
        System.out.printf("Current authenticated customer -> '%s' - '%s'\n", authCustomer.id(), authCustomer.email());
      };
    }
    if (event instanceof ParticipantFailed failed) {
      sagaMutation = orderSaga -> {
        orderSaga.getParticipants().forEach(participant -> {
          if (participant.getName() == failed.participant()) {
            participant.setStatus(SagaParticipantStatus.FAILURE);
          }
        });
        orderSaga.setStatus(SagaStatus.CANCELLING);
        orderSaga.setFailureCode(failed.failureCode());
        orderSaga.setFailureReason(failed.failureMessage());
      };
      action = () -> {
        UUID transactionId = UUID.randomUUID();

        InventoryTransactionCancelCommand inventoryCommand = InventoryTransactionCancelCommand.builder(sagaId, transactionId)
          .withProductId(failed.productId())
          .withOrderId(failed.orderId())
          .withFailureCode(failed.failureCode())
          .build();

        this.kafkaTemplate.send("order.order_transaction.inventory.commands", inventoryCommand)
          .whenComplete((result, exception) -> {
            if (exception == null) {
              logger.info("Cancel Order posted in topic \"{}\" successfully", result.getRecordMetadata().topic());
            }
          });
      };
    }
    return new SagaTransition(sagaMutation, action);
  }

  private SagaTransition handleCancelling(SagaEvent event) {
    Order deletedOrder = this.deleteOrderUseCase.execute(event.orderId());
    logger.info("Order with id \"{}\" deleted successfully", deletedOrder.getId());
    return new SagaTransition(saga -> saga.setStatus(SagaStatus.FAILED), () -> {});
  }
}
