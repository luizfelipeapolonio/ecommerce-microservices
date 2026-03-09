package com.felipe.ecommerce_order_service.infrastructure.external;

import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.saga.OrderSagaService;
import com.felipe.ecommerce_order_service.infrastructure.saga.SagaOrchestrator;
import com.felipe.kafka.saga.replies.InventoryTransactionReply;
import com.felipe.kafka.saga.replies.PaymentTransactionReply;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@KafkaListener(id = "order-transactions", topics = "order.order_transaction.replies", groupId = "order-service")
public class KafkaService {
  private final OrderSagaService orderSagaService;
  private final SagaOrchestrator sagaOrchestrator;
  private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);

  public KafkaService(OrderSagaService orderSagaService, SagaOrchestrator sagaOrchestrator) {
    this.orderSagaService = orderSagaService;
    this.sagaOrchestrator = sagaOrchestrator;
  }

  @Transactional // Keeps the Hibernate session/persistence context open until the method finishes executing
  @KafkaHandler
  void inventoryTransactionReplies(InventoryTransactionReply transactionReply) {
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
    OrderSaga saga = this.orderSagaService.findOrderSagaById(transactionReply.getSagaId());
    this.sagaOrchestrator.handle(saga, transactionReply);
  }

  @Transactional
  @KafkaHandler
  void paymentTransactionReplies(PaymentTransactionReply transactionReply) {
    logger.info(
      "\nReceived reply:\ncheckoutUrl: {}\nstatus: {}\nparticipant: {}\ncommand: {}\nfailureCode: {}\nfailureMessage: {}\ntransactionId: {}\norderId: {}",
      transactionReply.getCheckoutUrl(),
      transactionReply.getStatus(),
      transactionReply.getParticipant(),
      transactionReply.getCommand(),
      transactionReply.getFailureCode(),
      transactionReply.getFailureMessage(),
      transactionReply.getTransactionId(),
      transactionReply.getOrderId()
    );
    OrderSaga saga = this.orderSagaService.findOrderSagaById(transactionReply.getSagaId());
    this.sagaOrchestrator.handle(saga, transactionReply);
  }
}
