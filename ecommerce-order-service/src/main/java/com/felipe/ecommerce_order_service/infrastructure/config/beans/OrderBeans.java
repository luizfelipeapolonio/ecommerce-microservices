package com.felipe.ecommerce_order_service.infrastructure.config.beans;

import com.felipe.ecommerce_order_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.CreateOrderUseCase;
import com.felipe.ecommerce_order_service.core.application.usecases.DeleteOrderUseCase;
import com.felipe.ecommerce_order_service.core.application.usecases.FindOrderByIdUseCase;
import com.felipe.ecommerce_order_service.core.application.usecases.GetOrderByIdUseCase;
import com.felipe.ecommerce_order_service.core.application.usecases.GetOrderByIdWithItemsUseCase;
import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.CreateOrderUseCaseImpl;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.DeleteOrderUseCaseImpl;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.FindOrderByIdUseCaseImpl;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.GetOrderByIdUseCaseImpl;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.GetOrderByIdWithItemsUseCaseImpl;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.UpdateOrderUseCaseImpl;
import com.felipe.ecommerce_order_service.infrastructure.saga.state.impl.CancellingStateHandler;
import com.felipe.ecommerce_order_service.infrastructure.saga.state.impl.CommitingStateHandler;
import com.felipe.ecommerce_order_service.infrastructure.saga.state.impl.ProcessingStateHandler;
import com.felipe.ecommerce_order_service.infrastructure.saga.state.SagaState;
import com.felipe.ecommerce_order_service.infrastructure.saga.state.impl.StartedStateHandler;
import com.felipe.ecommerce_order_service.infrastructure.saga.state.impl.WaitingForPaymentStateHandler;
import com.felipe.ecommerce_order_service.infrastructure.saga.utils.InventoryTransitionDataHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderBeans {
  private final OrderGateway orderGateway;
  private final CustomerGateway customerGateway;
  private final CouponGateway couponGateway;
  private final InventoryTransitionDataHolder inventoryTransitionDataHolder;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public OrderBeans(OrderGateway orderGateway,
                    CustomerGateway customerGateway,
                    CouponGateway couponGateway,
                    InventoryTransitionDataHolder inventoryTransitionDataHolder,
                    KafkaTemplate<String, Object> kafkaTemplate) {
    this.orderGateway = orderGateway;
    this.customerGateway = customerGateway;
    this.couponGateway = couponGateway;
    this.inventoryTransitionDataHolder = inventoryTransitionDataHolder;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Bean
  public CreateOrderUseCase createOrderUseCase() {
    return new CreateOrderUseCaseImpl(this.orderGateway, this.customerGateway, this.couponGateway);
  }

  @Bean
  public DeleteOrderUseCase deleteOrderUseCase() {
    return new DeleteOrderUseCaseImpl(this.orderGateway);
  }

  @Bean
  public GetOrderByIdUseCase getOrderByIdUseCase() {
    return new GetOrderByIdUseCaseImpl(this.orderGateway);
  }

  @Bean
  public GetOrderByIdWithItemsUseCase getOrderByIdWithItemsUseCase() {
    return new GetOrderByIdWithItemsUseCaseImpl(this.orderGateway);
  }

  @Bean
  public FindOrderByIdUseCase findOrderByIdUseCase() {
    return new FindOrderByIdUseCaseImpl(this.orderGateway);
  }

  @Bean
  public UpdateOrderUseCase updateOrderUseCase() {
    return new UpdateOrderUseCaseImpl(this.orderGateway);
  }

  @Bean
  public SagaState startedStateHandler(UpdateOrderUseCase updateOrderUseCase) {
    return new StartedStateHandler(this.kafkaTemplate, this.customerGateway, updateOrderUseCase, this.inventoryTransitionDataHolder);
  }

  @Bean
  public SagaState processingStateHandler(UpdateOrderUseCase updateOrderUseCase) {
    return new ProcessingStateHandler(updateOrderUseCase, this.customerGateway, this.inventoryTransitionDataHolder, this.kafkaTemplate);
  }

  @Bean
  public SagaState waitingForPaymentStateHandler(UpdateOrderUseCase updateOrderUseCase) {
    return new WaitingForPaymentStateHandler(updateOrderUseCase, this.kafkaTemplate);
  }

  @Bean
  public SagaState commitingStateHandler(UpdateOrderUseCase updateOrderUseCase) {
    return new CommitingStateHandler(updateOrderUseCase, this.kafkaTemplate);
  }

  @Bean
  public SagaState cancellingStateHandler(DeleteOrderUseCase deleteOrderUseCase) {
    return new CancellingStateHandler(deleteOrderUseCase);
  }
}
