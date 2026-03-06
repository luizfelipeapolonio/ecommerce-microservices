package com.felipe.ecommerce_order_service.infrastructure.config.beans;

import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.CreateOrderUseCase;
import com.felipe.ecommerce_order_service.core.application.usecases.DeleteOrderUseCase;
import com.felipe.ecommerce_order_service.core.application.usecases.GetOrderByIdUseCase;
import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.CreateOrderUseCaseImpl;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.DeleteOrderUseCaseImpl;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.GetOrderByIdUseCaseImpl;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.UpdateOrderUseCaseImpl;
import com.felipe.ecommerce_order_service.infrastructure.saga.state.DefaultSagaState;
import com.felipe.ecommerce_order_service.infrastructure.saga.state.SagaState;
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
  public GetOrderByIdUseCase getOrderByIdUseCase() {
    return new GetOrderByIdUseCaseImpl(this.orderGateway);
  }

  @Bean
  public UpdateOrderUseCase updateOrderUseCase() {
    return new UpdateOrderUseCaseImpl(this.orderGateway);
  }

  @Bean
  public SagaState defaultSagaStateMachine(DeleteOrderUseCase deleteOrderUseCase, UpdateOrderUseCase updateOrderUseCase) {
    return new DefaultSagaState(this.kafkaTemplate, deleteOrderUseCase, updateOrderUseCase, this.customerGateway);
  }
}
