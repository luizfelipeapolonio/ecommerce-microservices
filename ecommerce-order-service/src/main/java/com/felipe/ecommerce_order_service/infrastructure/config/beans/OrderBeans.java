package com.felipe.ecommerce_order_service.infrastructure.config.beans;

import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderTransactionGateway;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.CreateOrderUseCase;
import com.felipe.ecommerce_order_service.core.application.usecases.DeleteOrderUseCase;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.CreateOrderUseCaseImpl;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.DeleteOrderUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OrderBeans {
  private final OrderGateway orderGateway;
  private final CustomerGateway customerGateway;
  private final OrderTransactionGateway orderTransactionGateway;

  public OrderBeans(OrderGateway orderGateway, CustomerGateway customerGateway,
                    OrderTransactionGateway orderTransactionGateway) {
    this.orderGateway = orderGateway;
    this.customerGateway = customerGateway;
    this.orderTransactionGateway = orderTransactionGateway;
  }

  @Bean
  public CreateOrderUseCase createOrderUseCase() {
    return new CreateOrderUseCaseImpl(this.orderGateway, this.customerGateway, this.orderTransactionGateway);
  }

  @Bean
  public DeleteOrderUseCase deleteOrderUseCase() {
    return new DeleteOrderUseCaseImpl(this.orderGateway);
  }
}
