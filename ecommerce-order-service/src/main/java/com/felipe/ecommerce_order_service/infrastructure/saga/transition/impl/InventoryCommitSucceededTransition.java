package com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl;

import com.felipe.ecommerce_order_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_order_service.core.application.dtos.UpdateOrderDTO;
import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.core.domain.enums.OrderStatus;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.kafka.saga.commands.ShippingTransactionCommitCommand;
import com.felipe.kafka.saga.replies.ReplyTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;
import java.util.function.Consumer;

public final class InventoryCommitSucceededTransition extends SagaTransition {
  private final ReplyTransaction reply;
  private final UpdateOrderUseCase updateOrderUseCase;
  private final CustomerGateway customerGateway;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(InventoryCommitSucceededTransition.class);

  public InventoryCommitSucceededTransition(ReplyTransaction reply,
                                            UpdateOrderUseCase updateOrderUseCase,
                                            CustomerGateway customerGateway,
                                            KafkaTemplate<String, Object> kafkaTemplate) {
    this.reply = reply;
    this.updateOrderUseCase = updateOrderUseCase;
    this.customerGateway = customerGateway;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  protected Consumer<OrderSaga> sagaMutation() {
    return saga -> {
      saga.markParticipantSuccess(ReplyTransaction.SagaParticipant.INVENTORY);
      saga.setStatus(SagaStatus.COMPLETED);
    };
  }

  @Override
  protected TriggerAction action() {
    return () -> {
      CustomerProfileDTO authCustomer = this.customerGateway.getCurrentAuthCustomer();
      UUID transactionId = UUID.randomUUID();

      ShippingTransactionCommitCommand shippingCommand = ShippingTransactionCommitCommand
        .startTransaction(this.reply.getSagaId(), transactionId)
        .withOrderId(this.reply.getOrderId())
        .withShipmentAddress(new ShippingTransactionCommitCommand.ShipmentAddress(
          authCustomer.address().street(),
          authCustomer.address().number(),
          authCustomer.address().complement(),
          authCustomer.address().district(),
          authCustomer.address().zipcode(),
          authCustomer.address().city(),
          authCustomer.address().state(),
          authCustomer.address().country()
        ))
        .build();

      this.kafkaTemplate.send("order.order_transaction.shipping.commands", shippingCommand)
        .whenComplete((result, exception) -> {
          if (exception == null) {
            logger.info("Shipping commit command posted successfully");
          }
        });

      UpdateOrderDTO orderDTO = new UpdateOrderDTO().updateStatus(OrderStatus.FINISHED);
      Order updatedOrder = this.updateOrderUseCase.execute(this.reply.getOrderId(), orderDTO);
      logger.info("Order \"{}\" finished successfully", updatedOrder.getId());
      logger.info("Saga \"{}\" completed successfully", this.reply.getSagaId());
    };
  }
}
