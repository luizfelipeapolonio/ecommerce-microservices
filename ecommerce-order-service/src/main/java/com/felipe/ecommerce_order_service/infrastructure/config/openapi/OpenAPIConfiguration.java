package com.felipe.ecommerce_order_service.infrastructure.config.openapi;

import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.core.domain.OrderItem;
import com.felipe.ecommerce_order_service.core.domain.enums.OrderStatus;
import com.felipe.ecommerce_order_service.infrastructure.dtos.CreateOrderDTOImpl;
import com.felipe.ecommerce_order_service.infrastructure.dtos.CreateOrderProductDTOImpl;
import com.felipe.ecommerce_order_service.infrastructure.dtos.OrderResponseDTO;
import com.felipe.ecommerce_order_service.infrastructure.dtos.OrderStatusDTO;
import com.felipe.ecommerce_order_service.infrastructure.dtos.SagaParticipantDetailsDTO;
import com.felipe.ecommerce_order_service.infrastructure.dtos.StartSagaDTO;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSagaParticipant;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaParticipantStatus;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.kafka.saga.replies.ReplyTransaction;
import com.felipe.openapi.OpenApiUtils;
import com.felipe.openapi.SchemaCustomizer;
import com.felipe.response.ResponseType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.felipe.openapi.OpenApiUtils.SCHEMAS_REF;

@OpenAPIDefinition
@Configuration
public class OpenAPIConfiguration {
  private final OpenApiUtils openApiUtils = OpenApiUtils.getInstanceWithCommonSchemas();
  public static final String BEARER_TOKEN_AUTH = "bearer_token_auth";

  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
      .servers(List.of(new Server().url("http://localhost:8086")))
      .info(new Info()
        .title("Order Service API")
        .description("This lists all the Order Service API calls")
        .version("1.0.0"))
      .tags(List.of(new Tag().name("Order").description("All order operations")))
      .components(new Components()
        .schemas(this.openApiUtils.getSchemas())
        .responses(this.openApiUtils.getResponses())
        .examples(this.openApiUtils.getExamples())
        .addSecuritySchemes(BEARER_TOKEN_AUTH, new SecurityScheme()
          .name(BEARER_TOKEN_AUTH)
          .scheme("bearer")
          .type(SecurityScheme.Type.HTTP)
          .bearerFormat("JWT")));
  }

  @Bean
  CommandLineRunner insertComponents() {
    return args -> {
      insertSchemas();
      insertExamples();
    };
  }

  private void insertSchemas() {
    ModelConverters modelConvertersInstance = ModelConverters.getInstance();
    this.openApiUtils.createSchemaFromClass(
      "StartSagaDTO",
      modelConvertersInstance,
      StartSagaDTO.class,
      SchemaCustomizer.withDefaults()
    );
    this.openApiUtils.createSchemaFromClass(
      "CreateOrderDTOImpl",
      modelConvertersInstance,
      CreateOrderDTOImpl.class,
      SchemaCustomizer.withDefaults()
    );
    this.openApiUtils.createSchemaFromClass(
      "CreateOrderProductDTOImpl",
      modelConvertersInstance,
      CreateOrderProductDTOImpl.class,
      SchemaCustomizer.withDefaults()
    );
    this.openApiUtils.createSchemaFromClass(
      "OrderSagaParticipant",
      modelConvertersInstance,
      OrderSagaParticipant.class,
      SchemaCustomizer.withDefaults()
    );
    this.openApiUtils.createSchemaFromClass(
      "SagaParticipantDetailsDTO",
      modelConvertersInstance,
      SagaParticipantDetailsDTO.class,
      SchemaCustomizer.withDefaults()
    );
    this.openApiUtils.createSchemaFromClass(
      "OrderStatusDTO",
      modelConvertersInstance,
      OrderStatusDTO.class,
      SchemaCustomizer.withDefaults()
    );
    this.openApiUtils.createSchemaFromClass(
      "Item",
      modelConvertersInstance,
      OrderResponseDTO.Item.class,
      SchemaCustomizer.withDefaults()
    );
    this.openApiUtils.createSchemaFromClass(
      "OrderResponseDTO",
      modelConvertersInstance,
      OrderResponseDTO.class,
      SchemaCustomizer.withDefaults()
    );
    this.openApiUtils.createSchema("ResponsePayload<StartSagaDTO>", schema -> {
      schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
      schema.addAllOfItem(new ObjectSchema()
        .addProperty("payload", new ObjectSchema().$ref(SCHEMAS_REF + "StartSagaDTO")));
    });
    this.openApiUtils.createSchema("ResponsePayload<OrderStatusDTO>", schema -> {
      schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
      schema.addAllOfItem(new ObjectSchema()
        .addProperty("payload", new ObjectSchema().$ref(SCHEMAS_REF + "OrderStatusDTO")));
    });
    this.openApiUtils.createSchema("ResponsePayload<OrderResponseDTO>", schema -> {
      schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
      schema.addAllOfItem(new ObjectSchema()
        .addProperty("payload", new ObjectSchema().$ref(SCHEMAS_REF + "OrderResponseDTO")));
    });
    this.openApiUtils.createSchema("ResponsePayload<List<OrderResponseDTO>>", schema -> {
      schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
      schema.addAllOfItem(new ObjectSchema().addProperty("payload", new ArraySchema()
        .items(new ObjectSchema().$ref(SCHEMAS_REF + "OrderResponseDTO"))));
    });
  }

  private void insertExamples() {
    StartSagaDTO startSagaDTO = new StartSagaDTO(
      "4ac98dba-b434-42fb-901b-905f18559010",
      "8270403b-3f73-4f50-aa8e-e3f6101a68e5",
      "PROCESSING",
      "http://localhost:8080/api/v1/orders/8270403b-3f73-4f50-aa8e-e3f6101a68e5/status"
    );

    OrderSagaParticipant sagaParticipant = new OrderSagaParticipant(ReplyTransaction.SagaParticipant.INVENTORY);
    sagaParticipant.setStatus(SagaParticipantStatus.FAILURE);
    sagaParticipant.setDetails("Failed by business exception");
    OrderSaga saga = new OrderSaga()
      .status(SagaStatus.STARTED)
      .failureReason(null)
      .addParticipant(sagaParticipant);
    OrderStatusDTO orderStatusDTO = new OrderStatusDTO(
      UUID.fromString("8270403b-3f73-4f50-aa8e-e3f6101a68e5"),
      saga,
      "https://checkoutUrl.com",
      "https://invoiceUrl.com",
      true
    );

    OrderItem orderItem1 = new OrderItem()
      .id(1L)
      .productName("Item 1")
      .productId(UUID.fromString("0008f5ca-70ab-48c6-a9c5-976b0191bbf4"))
      .finalPrice(new BigDecimal("150.00"))
      .quantity(1)
      .addedAt(LocalDateTime.parse("2026-05-29T18:13:00.657570300"));
    Order order1 = new Order()
      .id(UUID.fromString("95480447-11bd-4b55-a0af-821339812588"))
      .orderPrice(new BigDecimal("100.00"))
      .withCoupon(false)
      .couponCode(null)
      .shippingFee(new BigDecimal("20.00"))
      .checkoutUrl("https://checkoutUrl.com")
      .invoiceUrl("https://invoiceUrl.com")
      .customerId(UUID.fromString("1c6362db-13da-4419-8dbb-b6366e01ba9f"))
      .status(OrderStatus.PENDING)
      .createdAt(LocalDateTime.parse("2026-05-29T18:13:00.657570300"))
      .updatedAt(LocalDateTime.parse("2026-05-29T18:13:00.657570300"));
    order1.addItem(orderItem1);
    OrderResponseDTO orderResponseDTO = new OrderResponseDTO(order1);

    this.openApiUtils.createExample(
      "CreateOrderExample",
      ResponseType.SUCCESS,
      HttpStatus.ACCEPTED,
      "Request accepted. Starting order processing",
      startSagaDTO
    );
    this.openApiUtils.createExample(
      "GetOrderStatusExample",
      ResponseType.SUCCESS,
      HttpStatus.OK,
      "Status of order with id '8270403b-3f73-4f50-aa8e-e3f6101a68e5'",
      orderStatusDTO
    );
    this.openApiUtils.createExample(
      "GetOrderByIdExample",
      ResponseType.SUCCESS,
      HttpStatus.OK,
      "Order with id '95480447-11bd-4b55-a0af-821339812588'",
      orderResponseDTO
    );
    this.openApiUtils.createExample(
      "GetAllCustomerOrdersExample",
      ResponseType.SUCCESS,
      HttpStatus.OK,
      "All orders of customer with email 'customer@email.com'",
      List.of(orderResponseDTO)
    );
  }
}
