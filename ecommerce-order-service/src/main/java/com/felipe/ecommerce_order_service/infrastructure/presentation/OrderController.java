package com.felipe.ecommerce_order_service.infrastructure.presentation;

import com.felipe.ecommerce_order_service.core.application.usecases.CreateOrderUseCase;
import com.felipe.ecommerce_order_service.core.application.usecases.FindOrderByIdUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.infrastructure.dtos.CreateOrderDTOImpl;
import com.felipe.ecommerce_order_service.infrastructure.dtos.OrderStatusDTO;
import com.felipe.ecommerce_order_service.infrastructure.dtos.StartSagaDTO;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.saga.OrderSagaService;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
  private final CreateOrderUseCase createOrderUseCase;
  private final FindOrderByIdUseCase findOrderByIdUseCase;
  private final OrderSagaService orderSagaService;

  public OrderController(CreateOrderUseCase createOrderUseCase, FindOrderByIdUseCase findOrderByIdUseCase,
                         OrderSagaService orderSagaService) {
    this.createOrderUseCase = createOrderUseCase;
    this.findOrderByIdUseCase = findOrderByIdUseCase;
    this.orderSagaService = orderSagaService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.ACCEPTED)
  public ResponsePayload<StartSagaDTO> createOrder(@AuthenticationPrincipal Jwt jwt,
                                                   @RequestBody CreateOrderDTOImpl orderDTO) {
    Map<String, UUID> ids = this.createOrderUseCase.execute(orderDTO, jwt.getSubject());
    String sagaId = ids.get("sagaId").toString();
    String orderId = ids.get("orderId").toString();
    String statusUrl = String.format("http://localhost:8080/api/v1/orders/%s/status", orderId);
    return new ResponsePayload.Builder<StartSagaDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.ACCEPTED)
      .message("Requisição aceita. Iniciando processamento do pedido")
      .payload(new StartSagaDTO(sagaId, orderId, "PROCESSING", statusUrl))
      .build();
  }

  @GetMapping("/{orderId}/status")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<OrderStatusDTO> getOrderStatus(@PathVariable UUID orderId) {
    Optional<Order> order = this.findOrderByIdUseCase.execute(orderId);
    OrderSaga saga = this.orderSagaService.findOrderSagaByOrderId(orderId);
    OrderStatusDTO statusDTO = new OrderStatusDTO(
      orderId,
      saga.getStatus(),
      saga.getFailureReason(),
      order.map(Order::getCheckoutUrl).orElse(null),
      order.map(Order::getInvoiceUrl).orElse(null)
    );
    return new ResponsePayload.Builder<OrderStatusDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Status do pedido de id '" + orderId + "'")
      .payload(statusDTO)
      .build();
  }
}
