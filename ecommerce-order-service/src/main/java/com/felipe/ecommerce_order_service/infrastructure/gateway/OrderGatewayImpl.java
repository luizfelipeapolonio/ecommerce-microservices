package com.felipe.ecommerce_order_service.infrastructure.gateway;

import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.core.domain.enums.OrderStatus;
import com.felipe.ecommerce_order_service.infrastructure.mappers.OrderEntityMapper;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.OrderEntity;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSagaParticipant;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.persistence.repositories.OrderRepository;
import com.felipe.ecommerce_order_service.infrastructure.persistence.repositories.OrderSagaRepository;
import com.felipe.kafka.saga.commands.InventoryTransactionCreateCommand;
import com.felipe.kafka.saga.replies.ReplyTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderGatewayImpl implements OrderGateway {
  private final OrderRepository orderRepository;
  private final OrderEntityMapper orderEntityMapper;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final OrderSagaRepository orderSagaRepository;
  private static final Logger logger = LoggerFactory.getLogger(OrderGatewayImpl.class);

  public OrderGatewayImpl(OrderRepository orderRepository,
                          OrderEntityMapper orderEntityMapper,
                          KafkaTemplate<String, Object> kafkaTemplate,
                          OrderSagaRepository orderSagaRepository) {
    this.orderRepository = orderRepository;
    this.orderEntityMapper = orderEntityMapper;
    this.kafkaTemplate = kafkaTemplate;
    this.orderSagaRepository = orderSagaRepository;
  }

  @Override
  public Map<String, UUID> createOrder(UUID customerId, UUID productId, int productQuantity) {
    OrderEntity newOrder = new OrderEntity()
      .customerId(customerId)
      .productId(productId)
      .productQuantity(productQuantity)
      .status(OrderStatus.PENDING)
      .productName("PENDING")
      .finalPrice(new BigDecimal("0.00"));
    OrderEntity savedOrder = this.orderRepository.save(newOrder);

    // Start saga
    OrderSaga newSaga = new OrderSaga()
      .orderId(savedOrder.getId())
      .status(SagaStatus.STARTED)
      .addParticipant(new OrderSagaParticipant(ReplyTransaction.SagaParticipant.INVENTORY))
      .addParticipant(new OrderSagaParticipant(ReplyTransaction.SagaParticipant.PAYMENT));
    OrderSaga createdSaga = this.orderSagaRepository.save(newSaga);
    UUID transactionId = UUID.randomUUID();

    InventoryTransactionCreateCommand inventoryCommand = InventoryTransactionCreateCommand.startTransaction(createdSaga.getId(), transactionId)
      .withProductId(productId)
      .withOrderId(savedOrder.getId())
      .withProductQuantity(productQuantity)
      .build();

    this.kafkaTemplate.send("order.order_transaction.inventory.commands", inventoryCommand)
      .whenComplete((result, exception) -> {
        if (exception == null) {
          logger.info("Create Order posted in topic \"{}\" successfully", result.getRecordMetadata().topic());
        }
      });

    return Map.of(
      "orderId", savedOrder.getId(),
      "sagaId", newSaga.getId()
    );
  }

  @Override
  public Optional<Order> findOrderById(UUID orderId) {
    return this.orderRepository.findById(orderId).map(this.orderEntityMapper::toDomain);
  }

  @Override
  public void deleteOrder(UUID orderId) {
    this.orderRepository.deleteById(orderId);
  }

  @Override
  public Order updateOrder(Order order) {
    OrderEntity updatedOrder = this.orderEntityMapper.toEntity(order);
    return this.orderEntityMapper.toDomain(this.orderRepository.save(updatedOrder));
  }
}
