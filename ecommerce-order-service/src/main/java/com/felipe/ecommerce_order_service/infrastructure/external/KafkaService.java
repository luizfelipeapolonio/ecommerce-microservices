package com.felipe.ecommerce_order_service.infrastructure.external;

import com.felipe.ecommerce_order_service.core.application.usecases.DeleteOrderUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.infrastructure.exceptions.SagaNotFoundException;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaParticipantStatus;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.persistence.repositories.OrderSagaRepository;
import com.felipe.kafka.saga.commands.InventoryTransactionCancelCommand;
import com.felipe.kafka.saga.enums.FailureCode;
import com.felipe.kafka.saga.replies.InventoryTransactionReply;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KafkaService {
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final OrderSagaRepository orderSagaRepository;
  private final DeleteOrderUseCase deleteOrderUseCase;
  private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);
  private static final String ORDER_TRANSACTION_REPLIES_TOPIC = "order.order_transaction.replies";

  public KafkaService(KafkaTemplate<String, Object> kafkaTemplate, OrderSagaRepository orderSagaRepository,
                      DeleteOrderUseCase deleteOrderUseCase) {
    this.kafkaTemplate = kafkaTemplate;
    this.orderSagaRepository = orderSagaRepository;
    this.deleteOrderUseCase = deleteOrderUseCase;
  }

  @Transactional // Keeps the Hibernate session/persistence context open until the method finishes executing
  @KafkaListener(topics = ORDER_TRANSACTION_REPLIES_TOPIC, groupId = "order-service")
  void orderTransactionCreateReplies(InventoryTransactionReply transactionReply) {
    logger.info(
      "\nReceived reply:\nstatus: {}\nparticipant: {}\ncommand: {}\nfailureCode: {}\nfailureMessage: {}\ntransactionId: {}\norderId: {}",
      transactionReply.getStatus(),
      transactionReply.getParticipant(),
      transactionReply.getCommand(),
      transactionReply.getFailureCode(),
      transactionReply.getFailureMessage(),
      transactionReply.getTransactionId(),
      transactionReply.getOrderId()
    );

    OrderSaga saga = this.orderSagaRepository.findById(transactionReply.getSagaId())
      .orElseThrow(() -> new SagaNotFoundException(transactionReply.getSagaId()));

    SagaParticipantStatus participantStatus = transactionReply.getStatus() == InventoryTransactionReply.Status.SUCCESS
      ? SagaParticipantStatus.SUCCESS
      : SagaParticipantStatus.FAILURE;

    // Update saga participant status
    saga.getParticipants()
      .forEach(participant -> {
        if(participant.getName() == transactionReply.getParticipant()) {
          participant.setStatus(participantStatus);
        }
      });
    OrderSaga updatedSaga = this.orderSagaRepository.save(saga);

    if(transactionReply.getCommand() == InventoryTransactionReply.Command.CREATE) {
      if(transactionReply.getStatus() == InventoryTransactionReply.Status.FAILURE) {
        logger.error(
          "\nFailure reply:\nparticipant: {}\ncommand: {}\nfailureCode: {}\nfailureMessage: {}\norderId: {}",
          transactionReply.getParticipant(),
          transactionReply.getCommand(),
          transactionReply.getFailureCode(),
          transactionReply.getFailureMessage(),
          transactionReply.getOrderId()
        );
        updatedSaga.setFailureCode(transactionReply.getFailureCode());
        updatedSaga.setFailureReason(transactionReply.getFailureMessage());
        updatedSaga.setStatus(SagaStatus.CANCELLING);
        this.orderSagaRepository.save(updatedSaga);

        cancelOrderTransaction(updatedSaga, transactionReply.getProductId(), transactionReply.getOrderId(), transactionReply.getFailureCode());
      } else {
        if(isAllParticipantsReady(updatedSaga)) {
          System.out.println("TRIGGER COMMIT");
        }
      }
    }
    if(transactionReply.getCommand() == InventoryTransactionReply.Command.CANCEL) {
      Order deletedOrder = this.deleteOrderUseCase.execute(transactionReply.getOrderId());
      updatedSaga.setStatus(SagaStatus.FAILED);
      logger.info("Order with id \"{}\" deleted successfully", deletedOrder.getId());
    }
  }

  private void cancelOrderTransaction(OrderSaga saga, UUID productId, UUID orderId, FailureCode failureCode) {
    UUID sagaId = saga.getId();
    UUID transactionId = UUID.randomUUID();
    InventoryTransactionCancelCommand inventoryCommand = InventoryTransactionCancelCommand.builder(sagaId, transactionId)
      .withProductId(productId)
      .withOrderId(orderId)
      .withFailureCode(failureCode)
      .build();

    this.kafkaTemplate.send("order.order_transaction.inventory.commands", inventoryCommand)
      .whenComplete((result, exception) -> {
        if(exception != null) {
          logger.info("Cancel Order posted in topic \"{}\" successfully", result.getRecordMetadata().topic());
        }
      });
  }

  private boolean isAllParticipantsReady(OrderSaga saga) {
    return saga.getParticipants()
      .stream()
      .allMatch(participant -> participant.getStatus() == SagaParticipantStatus.SUCCESS);
  }
}
