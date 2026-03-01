package com.felipe.ecommerce_order_service.infrastructure.config.beans;

import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.CreateOrderUseCase;
import com.felipe.ecommerce_order_service.core.application.usecases.DeleteOrderUseCase;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.CreateOrderUseCaseImpl;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.DeleteOrderUseCaseImpl;
import com.felipe.ecommerce_order_service.infrastructure.saga.DefaultSagaStateMachine;
import com.felipe.ecommerce_order_service.infrastructure.saga.SagaStateMachine;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderBeans {
  private final OrderGateway orderGateway;
  private final CustomerGateway customerGateway;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public OrderBeans(OrderGateway orderGateway, CustomerGateway customerGateway,
                    KafkaTemplate<String, Object> kafkaTemplate) {
    this.orderGateway = orderGateway;
    this.customerGateway = customerGateway;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Bean
  public CreateOrderUseCase createOrderUseCase() {
    return new CreateOrderUseCaseImpl(this.orderGateway, this.customerGateway);
  }

  @Bean
  public DeleteOrderUseCase deleteOrderUseCase() {
    return new DeleteOrderUseCaseImpl(this.orderGateway);
  }

  @Bean
  public SagaStateMachine defaultSagaStateMachine(DeleteOrderUseCase deleteOrderUseCase) {
    return new DefaultSagaStateMachine(this.kafkaTemplate, deleteOrderUseCase, this.customerGateway);
  }
}
