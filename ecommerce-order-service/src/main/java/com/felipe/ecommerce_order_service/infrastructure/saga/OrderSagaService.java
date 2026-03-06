package com.felipe.ecommerce_order_service.infrastructure.saga;

import com.felipe.ecommerce_order_service.infrastructure.exceptions.SagaNotFoundException;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.repositories.OrderSagaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderSagaService {
  private final OrderSagaRepository orderSagaRepository;

  public OrderSagaService(OrderSagaRepository orderSagaRepository) {
    this.orderSagaRepository = orderSagaRepository;
  }

  public OrderSaga findOrderSagaById(UUID orderSagaId) {
    return this.orderSagaRepository.findById(orderSagaId)
      .orElseThrow(() -> new SagaNotFoundException(orderSagaId));
  }

  public OrderSaga findOrderSagaByOrderId(UUID orderId) {
    return this.orderSagaRepository.findByOrderId(orderId)
      .orElseThrow(() -> new SagaNotFoundException("Saga with orderId '" + orderId + "' not found"));
  }

  public OrderSaga updateOrderSaga(OrderSaga orderSaga) {
    return this.orderSagaRepository.save(orderSaga);
  }
}
