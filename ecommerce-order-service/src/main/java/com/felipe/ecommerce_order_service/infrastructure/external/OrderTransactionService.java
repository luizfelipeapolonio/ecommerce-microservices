package com.felipe.ecommerce_order_service.infrastructure.external;

import com.felipe.ecommerce_order_service.core.application.gateway.OrderTransactionGateway;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSagaParticipant;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.persistence.repositories.OrderSagaRepository;
import com.felipe.kafka.saga.commands.InventoryTransactionCreateCommand;
import com.felipe.kafka.saga.enums.SagaParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderTransactionService implements OrderTransactionGateway {
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final OrderSagaRepository orderSagaRepository;
  private static final Logger logger = LoggerFactory.getLogger(OrderTransactionService.class);

  public OrderTransactionService(KafkaTemplate<String, Object> kafkaTemplate, OrderSagaRepository orderSagaRepository) {
    this.kafkaTemplate = kafkaTemplate;
    this.orderSagaRepository = orderSagaRepository;
  }

  @Override
  public UUID executeOrderTransaction(UUID orderId, UUID productId, int productQuantity) {
    // Start saga
    OrderSaga newSaga = new OrderSaga()
      .orderId(orderId)
      .status(SagaStatus.STARTED)
      .addParticipant(new OrderSagaParticipant(SagaParticipant.INVENTORY))
      .addParticipant(new OrderSagaParticipant(SagaParticipant.PAYMENT));
    OrderSaga createdSaga = this.orderSagaRepository.save(newSaga);
    UUID transactionId = UUID.randomUUID();

    InventoryTransactionCreateCommand inventoryCommand = InventoryTransactionCreateCommand.startTransaction(createdSaga.getId(), transactionId)
      .withProductId(productId)
      .withOrderId(orderId)
      .withProductQuantity(productQuantity)
      .build();

    this.kafkaTemplate.send("order.order_transaction.inventory.commands", inventoryCommand)
      .whenComplete((result, exception) -> {
        if(exception != null) {
          logger.info("Create Order posted in topic \"{}\" successfully", result.getRecordMetadata().topic());
        }
      });
    return createdSaga.getId();
  }
}
