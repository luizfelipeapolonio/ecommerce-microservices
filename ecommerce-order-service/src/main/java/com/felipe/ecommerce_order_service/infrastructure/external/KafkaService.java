package com.felipe.ecommerce_order_service.infrastructure.external;

import com.felipe.ecommerce_order_service.infrastructure.exceptions.SagaNotFoundException;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaParticipantStatus;
import com.felipe.ecommerce_order_service.infrastructure.persistence.repositories.OrderSagaRepository;
import com.felipe.ecommerce_order_service.infrastructure.saga.DefaultSagaStateMachine;
import com.felipe.ecommerce_order_service.infrastructure.saga.event.SagaEvent;
import com.felipe.ecommerce_order_service.infrastructure.saga.SagaTransition;
import com.felipe.kafka.saga.replies.InventoryTransactionReply;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
  private final OrderSagaRepository orderSagaRepository;
  private final DefaultSagaStateMachine defaultSagaStateMachine;
  private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);
  private static final String ORDER_TRANSACTION_REPLIES_TOPIC = "order.order_transaction.replies";

  public KafkaService(OrderSagaRepository orderSagaRepository, DefaultSagaStateMachine defaultSagaStateMachine) {
    this.orderSagaRepository = orderSagaRepository;
    this.defaultSagaStateMachine = defaultSagaStateMachine;
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
    SagaEvent event = SagaEvent.mapReplyToEvent(transactionReply);
    OrderSaga saga = this.orderSagaRepository.findById(transactionReply.getSagaId())
      .orElseThrow(() -> new SagaNotFoundException(transactionReply.getSagaId()));

    // Handle and mutate saga status
    SagaTransition transition = this.defaultSagaStateMachine.handle(saga, event);
    transition.apply(saga); // apply changes made in saga
    this.orderSagaRepository.save(saga);
  }

  private boolean isAllParticipantsReady(OrderSaga saga) {
    return saga.getParticipants()
      .stream()
      .allMatch(participant -> participant.getStatus() == SagaParticipantStatus.SUCCESS);
  }
}
