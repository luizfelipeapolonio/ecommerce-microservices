package com.felipe.ecommerce_order_service.infrastructure.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipe.ecommerce_order_service.core.application.usecases.CreateOrderUseCase;
import com.felipe.ecommerce_order_service.core.application.usecases.FindOrderByIdUseCase;
import com.felipe.ecommerce_order_service.core.application.usecases.GetOrderByIdWithItemsUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.infrastructure.dtos.CreateOrderDTOImpl;
import com.felipe.ecommerce_order_service.infrastructure.dtos.OrderResponseDTO;
import com.felipe.ecommerce_order_service.infrastructure.dtos.OrderStatusDTO;
import com.felipe.ecommerce_order_service.infrastructure.dtos.StartSagaDTO;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSagaParticipant;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.OrderSagaService;
import com.felipe.ecommerce_order_service.testutils.DataMock;
import com.felipe.ecommerce_order_service.testutils.OAuth2TestMockConfiguration;
import com.felipe.kafka.saga.replies.ReplyTransaction;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@ExtendWith(MockitoExtension.class)
@Import({OAuth2TestMockConfiguration.class})
@EnableAutoConfiguration(exclude = {KafkaAutoConfiguration.class})
class OrderControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  private CreateOrderUseCase createOrderUseCase;

  @MockitoBean
  private FindOrderByIdUseCase findOrderByIdUseCase;

  @MockitoBean(name = "getOrderByIdWithItemsAuthenticated")
  private GetOrderByIdWithItemsUseCase getOrderByIdWithItemsAuthenticatedUseCase;

  @MockitoBean(name = "getOrderByIdWithItems")
  private GetOrderByIdWithItemsUseCase getOrderByIdWithItemsUseCase;

  @MockitoBean
  private OrderSagaService orderSagaService;

  private DataMock dataMock;
  private static final String BASE_URL = "/api/v1/orders";
  private static final String CUSTOMER_EMAIL = "test@email.com";

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createOrderSuccess - Should return a ResponsePayload with a success response")
  void createOrderSuccess() throws Exception {
    CreateOrderDTOImpl orderDTO = new CreateOrderDTOImpl(List.of(), "");
    UUID orderId = UUID.fromString("39995a63-8e6e-4ace-8bb8-4c4018124165");
    UUID sagaId = UUID.fromString("042e650d-6200-4fc6-833d-bf0c1dba3c53");
    String statusUrl = String.format("http://localhost:8080/api/v1/orders/%s/status", orderId);

    var response = new ResponsePayload.Builder<StartSagaDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.ACCEPTED)
      .message("Requisição aceita. Iniciando processamento do pedido")
      .payload(new StartSagaDTO(sagaId.toString(), orderId.toString(), "PROCESSING", statusUrl))
      .build();

    String jsonRequestBody = this.objectMapper.writeValueAsString(orderDTO);
    String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.createOrderUseCase.execute(orderDTO, CUSTOMER_EMAIL))
      .thenReturn(Map.of("orderId", orderId, "sagaId", sagaId));

    this.mockMvc.perform(post(BASE_URL)
      .with(jwt().jwt(jwt -> jwt.subject(CUSTOMER_EMAIL)))
      .contentType(MediaType.APPLICATION_JSON).content(jsonRequestBody)
      .accept(MediaType.APPLICATION_JSON))
      .andExpectAll(status().isAccepted(), content().json(jsonResponseBody));

    verify(this.createOrderUseCase, times(1)).execute(orderDTO, CUSTOMER_EMAIL);
  }

  @Test
  @DisplayName("getOrderStatusSuccess - Should return a ResponsePayload with a success response")
  void getOrderStatusSuccess() throws Exception {
    Optional<Order> order = Optional.of(this.dataMock.getOrdersDomain().getFirst());
    UUID orderId = UUID.fromString("39995a63-8e6e-4ace-8bb8-4c4018124165");
    boolean withDetails = true;
    OrderSaga saga = new OrderSaga()
      .status(SagaStatus.STARTED)
      .addParticipant(new OrderSagaParticipant(ReplyTransaction.SagaParticipant.INVENTORY));
    OrderStatusDTO orderStatus = new OrderStatusDTO(
      orderId,
      saga,
      order.get().getCheckoutUrl(),
      order.get().getInvoiceUrl(),
      withDetails
    );

    var response = new ResponsePayload.Builder<OrderStatusDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Status do pedido de id '" + orderId + "'")
      .payload(orderStatus)
      .build();

    String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.findOrderByIdUseCase.execute(orderId)).thenReturn(order);
    when(this.orderSagaService.findOrderSagaByOrderId(orderId, withDetails)).thenReturn(saga);

    this.mockMvc.perform(get(BASE_URL + "/{orderId}/status?withDetails={withDetails}", orderId, withDetails)
      .with(jwt().jwt(jwt -> jwt.subject(CUSTOMER_EMAIL)))
      .accept(MediaType.APPLICATION_JSON))
      .andExpectAll(status().isOk(), content().json(jsonResponseBody));

    verify(this.findOrderByIdUseCase, times(1)).execute(orderId);
    verify(this.orderSagaService, times(1)).findOrderSagaByOrderId(orderId, withDetails);
  }

  @Test
  @DisplayName("getOrderByIdSuccess - Should return a ResponsePayload with a success response")
  void getOrderByIdSuccess() throws Exception {
    UUID orderId = UUID.fromString("39995a63-8e6e-4ace-8bb8-4c4018124165");
    Order order = this.dataMock.getOrdersDomain().getFirst();
    var response = new ResponsePayload.Builder<OrderResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Pedido de id '" + orderId + "'")
      .payload(new OrderResponseDTO(order))
      .build();
    String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.getOrderByIdWithItemsAuthenticatedUseCase.execute(orderId, CUSTOMER_EMAIL)).thenReturn(order);

    this.mockMvc.perform(get(BASE_URL + "/{orderId}", orderId)
      .with(jwt()
        .jwt(jwt -> jwt
          .subject(CUSTOMER_EMAIL)
          .claim("authorities", "ROLE_USER")))
      .accept(MediaType.APPLICATION_JSON))
      .andExpectAll(status().isOk(), content().json(jsonResponseBody));

    verify(this.getOrderByIdWithItemsAuthenticatedUseCase, times(1)).execute(orderId, CUSTOMER_EMAIL);
  }
}