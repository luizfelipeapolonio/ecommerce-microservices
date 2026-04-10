package com.felipe.ecommerce_order_service.infrastructure.gateway;

import com.felipe.ecommerce_order_service.core.application.dtos.CreateOrderDTO;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.core.domain.enums.OrderStatus;
import com.felipe.ecommerce_order_service.infrastructure.mappers.OrderEntityMapper;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.OrderEntity;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.OrderItemEntity;
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
import java.util.List;
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
  public Map<String, UUID> createOrder(UUID customerId, CreateOrderDTO orderDTO) {
    OrderEntity newOrder = new OrderEntity()
      .customerId(customerId)
      .status(OrderStatus.PENDING)
      .withCoupon(orderDTO.couponCode() != null)
      .couponCode(orderDTO.couponCode() == null ?  null : orderDTO.couponCode())
      .orderPrice(new BigDecimal("0.00"));

    orderDTO.products().forEach(product ->
      newOrder.addItem(new OrderItemEntity()
        .productName("PENDING")
        .productId(product.id())
        .quantity(product.quantity())
        .finalPrice(new BigDecimal("0.00"))
      ));
    OrderEntity savedOrder = this.orderRepository.save(newOrder);

    // Start saga
    OrderSaga newSaga = new OrderSaga()
      .orderId(savedOrder.getId())
      .status(SagaStatus.STARTED)
      .addParticipant(new OrderSagaParticipant(ReplyTransaction.SagaParticipant.INVENTORY))
      .addParticipant(new OrderSagaParticipant(ReplyTransaction.SagaParticipant.PAYMENT))
      .addParticipant(new OrderSagaParticipant(ReplyTransaction.SagaParticipant.DISCOUNT));

    OrderSaga createdSaga = this.orderSagaRepository.save(newSaga);
    UUID transactionId = UUID.randomUUID();
    postInventoryTransactionCommand(createdSaga.getId(), transactionId, savedOrder);

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
  public Optional<Order> findOrderByIdWithItems(UUID orderId) {
    return this.orderRepository.findByIdWithItems(orderId).map(this.orderEntityMapper::toDomain);
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

  private List<InventoryTransactionCreateCommand.ProductData> inventoryTransactionProducts(OrderEntity order) {
    return order.getItems()
      .stream()
      .map(item -> new InventoryTransactionCreateCommand.ProductData(item.getProductId(), item.getQuantity()))
      .toList();
  }

  private void postInventoryTransactionCommand(UUID sagaId, UUID transactionId, OrderEntity order) {
    List<InventoryTransactionCreateCommand.ProductData> products = inventoryTransactionProducts(order);
    InventoryTransactionCreateCommand inventoryCommand = InventoryTransactionCreateCommand
      .startTransaction(sagaId, transactionId)
      .withOrderId(order.getId())
      .withProducts(products)
      .build();

    this.kafkaTemplate.send("order.order_transaction.inventory.commands", inventoryCommand)
      .whenComplete((result, exception) -> {
        if (exception == null) {
          logger.info("Create Order posted in topic \"{}\" successfully", result.getRecordMetadata().topic());
        }
      });
  }
}
