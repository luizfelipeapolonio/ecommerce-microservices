package com.felipe.ecommerce_order_service.infrastructure.config.openapi;

import com.felipe.ecommerce_order_service.infrastructure.dtos.CreateOrderDTOImpl;
import com.felipe.ecommerce_order_service.infrastructure.dtos.OrderResponseDTO;
import com.felipe.ecommerce_order_service.infrastructure.dtos.OrderStatusDTO;
import com.felipe.ecommerce_order_service.infrastructure.dtos.StartSagaDTO;
import com.felipe.response.ResponsePayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SecurityRequirement(name = OpenAPIConfiguration.BEARER_TOKEN_AUTH)
@Tag(name = "Order")
public interface OrderApi {

  @Operation(
    operationId = "CreateOrder",
    summary = "Create an order",
    description = "Create an order",
    requestBody = @RequestBody(description = "Request body to create an order"),
    responses = {
      @ApiResponse(responseCode = "202", description = "Returns a ResponsePayload with initial order status", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<StartSagaDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "CreateOrderExample")
        })
      }),
      @ApiResponse(responseCode = "422", ref = "ValidationErrors"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<StartSagaDTO> createOrder(
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Authenticated user", schema = @Schema(name = "Jwt", type = "object"))
    @AuthenticationPrincipal Jwt jwt,
    @Parameter(name = "CreateOrderDTOImpl", required = true)
    @org.springframework.web.bind.annotation.RequestBody CreateOrderDTOImpl orderDTO
  );

  @Operation(
    operationId = "getAllCustomerOrders",
    summary = "Get all customer orders",
    description = "Get all customer orders",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with a list of orders", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<List<OrderResponseDTO>>"), examples = {
          @ExampleObject(name = "Success response", ref = "GetAllCustomerOrdersExample")
        })
      }),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<List<OrderResponseDTO>> getAllCustomerOrders(
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Authenticated user", schema = @Schema(name = "Jwt", type = "object"))
    @AuthenticationPrincipal Jwt jwt
  );

  @Operation(
    operationId = "GetOrderStatus",
    summary = "Get the order status",
    description = "Get the order status",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with the order status", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<OrderStatusDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "GetOrderStatusExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<OrderStatusDTO> getOrderStatus(
    @Parameter(in = ParameterIn.PATH, name = "orderId", description = "Order id", required = true, schema = @Schema(type = "string", example = "da4dd8a3-a821-4350-9af2-c5b8f3801330"))
    @PathVariable UUID orderId,
    @Parameter(in = ParameterIn.QUERY, name = "withDetails", description = "Order details", required = false, schema = @Schema(type = "boolean", example = "true"))
    @RequestParam(name = "withDetails", defaultValue = "false") boolean withDetails
  );

  @Operation(
    operationId = "GetOrderById",
    summary = "Get an order by id",
    description = "Get an order by id",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with order info", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<OrderResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "GetOrderByIdExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<OrderResponseDTO> getOrderById(
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Authenticated user", schema = @Schema(name = "Jwt", type = "object"))
    @AuthenticationPrincipal Jwt jwt,
    @Parameter(in = ParameterIn.PATH, name = "orderId", description = "Order id", required = true, schema = @Schema(type = "string", example = "da4dd8a3-a821-4350-9af2-c5b8f3801330"))
    @PathVariable UUID orderId
  );
}
