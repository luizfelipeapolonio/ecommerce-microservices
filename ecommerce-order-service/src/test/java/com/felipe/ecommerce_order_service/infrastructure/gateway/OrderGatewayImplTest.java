package com.felipe.ecommerce_order_service.infrastructure.gateway;

import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.core.domain.enums.OrderStatus;
import com.felipe.ecommerce_order_service.infrastructure.dtos.CreateOrderDTOImpl;
import com.felipe.ecommerce_order_service.infrastructure.dtos.CreateOrderProductDTOImpl;
import com.felipe.ecommerce_order_service.infrastructure.mappers.OrderEntityMapper;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.OrderEntity;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.repositories.OrderRepository;
import com.felipe.ecommerce_order_service.infrastructure.persistence.repositories.OrderSagaRepository;
import com.felipe.ecommerce_order_service.testutils.DataMock;
import com.felipe.kafka.saga.commands.InventoryTransactionCreateCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderGatewayImplTest {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private OrderEntityMapper orderEntityMapper;

  @Mock
  private OrderSagaRepository orderSagaRepository;

  @Mock
  private KafkaTemplate<String, Object> kafkaTemplate;

  @InjectMocks
  private OrderGatewayImpl orderGateway;

  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createOrderSuccess - Should successfully create an order")
  void createOrderSuccess() {
    OrderEntity orderEntity = this.dataMock.getOrdersEntity().getFirst();
    CreateOrderDTOImpl createOrderDTO = new CreateOrderDTOImpl(
      List.of(
        new CreateOrderProductDTOImpl(UUID.fromString("0008f5ca-70ab-48c6-a9c5-976b0191bbf4"), 1L)
      ),
      "COUPON20"
    );
    UUID customerId = UUID.fromString(this.dataMock.getCustomerProfileDTO().id());
    OrderSaga saga = new OrderSaga().id(UUID.fromString("afa30f30-0716-4383-8401-c24ed0e5cab2"));
    String inventoryTopic = "order.order_transaction.inventory.commands";

    when(this.orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);
    when(this.orderSagaRepository.save(any(OrderSaga.class))).thenReturn(saga);
    when(this.kafkaTemplate.send(eq(inventoryTopic), any(InventoryTransactionCreateCommand.class)))
      .thenReturn(new CompletableFuture<>());

    Map<String, UUID> orderResponse = this.orderGateway.createOrder(customerId, createOrderDTO);

    assertThat(orderResponse).containsKeys("sagaId", "orderId");
    assertThat(orderResponse.get("sagaId")).isEqualTo(saga.getId());
    assertThat(orderResponse.get("orderId")).isEqualTo(orderEntity.getId());

    verify(this.orderRepository, times(1)).save(assertArg(savedOrder -> {
      assertThat(savedOrder.getCustomerId()).isEqualTo(customerId);
      assertThat(savedOrder.getOrderPrice()).isEqualTo(new BigDecimal("0.00"));
      assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.PENDING.getText());
      assertThat(savedOrder.isWithCoupon()).isTrue();
      assertThat(savedOrder.getCouponCode()).isEqualTo(createOrderDTO.couponCode());
      assertThat(savedOrder.getItems().getFirst().getProductName()).isEqualTo("PENDING");
      assertThat(savedOrder.getItems().getFirst().getProductId()).isEqualTo(createOrderDTO.products().getFirst().id());
      assertThat(savedOrder.getItems().getFirst().getQuantity()).isEqualTo(createOrderDTO.products().getFirst().quantity());
      assertThat(savedOrder.getItems().getFirst().getFinalPrice()).isEqualTo(new BigDecimal("0.00"));
    }));
    verify(this.orderSagaRepository, times(1)).save(any(OrderSaga.class));
    verify(this.kafkaTemplate, times(1)).send(eq(inventoryTopic), any(InventoryTransactionCreateCommand.class));
  }

  @Test
  @DisplayName("findOrderByIdSuccess - Should find an order by id and return an Optional of Order")
  void findOrderByIdSuccess() {
    Order orderDomain = this.dataMock.getOrdersDomain().getFirst();
    OrderEntity orderEntity = this.dataMock.getOrdersEntity().getFirst();

    when(this.orderRepository.findById(orderEntity.getId())).thenReturn(Optional.of(orderEntity));
    when(this.orderEntityMapper.toDomain(orderEntity)).thenReturn(orderDomain);

    Optional<Order> foundOrder = this.orderGateway.findOrderById(orderEntity.getId());

    assertThat(foundOrder).isPresent();
    assertThat(foundOrder.get()).usingRecursiveComparison().isEqualTo(orderDomain);

    verify(this.orderRepository, times(1)).findById(orderEntity.getId());
    verify(this.orderEntityMapper, times(1)).toDomain(orderEntity);
  }

  @Test
  @DisplayName("findOrderByIdWithItemsSuccess - Should find an order with items by id and return an Optional of Order")
  void findOrderByIdWithItemsSuccess() {
    Order orderDomain = this.dataMock.getOrdersDomain().getFirst();
    OrderEntity orderEntity = this.dataMock.getOrdersEntity().getFirst();

    when(this.orderRepository.findByIdWithItems(orderEntity.getId())).thenReturn(Optional.of(orderEntity));
    when(this.orderEntityMapper.toDomain(orderEntity)).thenReturn(orderDomain);

    Optional<Order> foundOrder = this.orderGateway.findOrderByIdWithItems(orderEntity.getId());

    assertThat(foundOrder).isPresent();
    assertThat(foundOrder.get()).usingRecursiveComparison().isEqualTo(orderDomain);

    verify(this.orderRepository, times(1)).findByIdWithItems(orderEntity.getId());
    verify(this.orderEntityMapper, times(1)).toDomain(orderEntity);
  }

  @Test
  @DisplayName("findOrderByIdAndCustomerIdWithItemsSuccess - Should find an order with items by id and customer id and return an Optional of Order")
  void findOrderByIdAndCustomerIdWithItemsSuccess() {
    UUID customerId = UUID.fromString(this.dataMock.getCustomerProfileDTO().id());
    Order orderDomain = this.dataMock.getOrdersDomain().getFirst();
    OrderEntity orderEntity = this.dataMock.getOrdersEntity().getFirst();

    when(this.orderRepository.findByIdAndCustomerIdWithItems(orderEntity.getId(), customerId))
      .thenReturn(Optional.of(orderEntity));
    when(this.orderEntityMapper.toDomain(orderEntity)).thenReturn(orderDomain);

    Optional<Order> foundOrder = this.orderGateway.findOrderByIdAndCustomerIdWithItems(orderEntity.getId(), customerId);

    assertThat(foundOrder).isPresent();
    assertThat(foundOrder.get()).usingRecursiveComparison().isEqualTo(orderDomain);

    verify(this.orderRepository, times(1)).findByIdAndCustomerIdWithItems(orderEntity.getId(), customerId);
    verify(this.orderEntityMapper, times(1)).toDomain(orderEntity);
  }

  @Test
  @DisplayName("deleteOrderSuccess - Should successfully delete an Order")
  void deleteOrderSuccess() {
    OrderEntity orderEntity = this.dataMock.getOrdersEntity().getFirst();

    doNothing().when(this.orderRepository).deleteById(orderEntity.getId());

    this.orderGateway.deleteOrder(orderEntity.getId());

    verify(this.orderRepository, times(1)).deleteById(orderEntity.getId());
  }

  @Test
  @DisplayName("updateOrderSuccess - Should successfully update an order and return it")
  void updateOrderSuccess() {
    OrderEntity orderEntity = this.dataMock.getOrdersEntity().getFirst();
    Order orderDomain = this.dataMock.getOrdersDomain().getFirst();

    when(this.orderEntityMapper.toEntity(orderDomain)).thenReturn(orderEntity);
    when(this.orderRepository.save(orderEntity)).thenReturn(orderEntity);
    when(this.orderEntityMapper.toDomain(orderEntity)).thenReturn(orderDomain);

    Order updatedOrder = this.orderGateway.updateOrder(orderDomain);

    assertThat(updatedOrder).usingRecursiveComparison().isEqualTo(orderDomain);

    verify(this.orderEntityMapper, times(1)).toEntity(orderDomain);
    verify(this.orderRepository, times(1)).save(orderEntity);
    verify(this.orderEntityMapper, times(1)).toDomain(orderEntity);
  }
}