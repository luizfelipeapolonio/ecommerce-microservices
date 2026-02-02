package com.felipe.ecommerce_cart_service.infrastructure.config.openapi;

import com.felipe.ecommerce_cart_service.infrastructure.dtos.cart.AddItemToCartDTO;
import com.felipe.ecommerce_cart_service.infrastructure.dtos.cart.CartItemResponseDTO;
import com.felipe.ecommerce_cart_service.infrastructure.dtos.cart.CartResponseDTO;
import com.felipe.ecommerce_cart_service.infrastructure.dtos.cart.CreateCartDTO;
import com.felipe.ecommerce_cart_service.infrastructure.dtos.cart.UpdateCartItemDTO;
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
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SecurityRequirement(name = OpenAPIConfiguration.BEARER_TOKEN_AUTH)
@Tag(name = "Cart")
public interface CartApi {

  @Operation(
    operationId = "createCart",
    summary = "Create a cart",
    description = "Create a customer cart",
    requestBody = @RequestBody(description = "Request body to create a cart"),
    responses = {
      @ApiResponse(responseCode = "201", description = "Returns a ResponsePayload with the created cart", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<CartResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "CreateCartResponseExample")
        })
      }),
      @ApiResponse(responseCode = "422", ref = "ValidationErrors"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<CartResponseDTO> createCart(
    @Parameter(name = "CreateCartDTO", required = true)
    @Valid @org.springframework.web.bind.annotation.RequestBody CreateCartDTO createCartDTO
  );

  @Operation(
    operationId = "addItemToCart",
    summary = "Add an item to cart",
    description = "Add an item to cart",
    requestBody = @RequestBody(description = "Request body to add an item to cart"),
    responses = {
      @ApiResponse(responseCode = "201", description = "Returns a ResponsePayload with the added cart item", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<CartItemResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "AddItemToCartExample")
        })
      }),
      @ApiResponse(responseCode = "409", description = "Returns an error response if the item is already in the cart", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<Void>"), examples = {
          @ExampleObject(name = "Error response", ref = "ExistingCartItemExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<CartItemResponseDTO> addItemToCart(
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Authenticated user", schema = @Schema(name = "Jwt", type = "object"))
    @AuthenticationPrincipal Jwt jwt,
    @Parameter(name = "AddItemToCartDTO", required = true)
    @Valid @org.springframework.web.bind.annotation.RequestBody AddItemToCartDTO itemDTO
  );

  @Operation(
    operationId = "getAllCartItems",
    summary = "Get all cart items",
    description = "Get all cart items",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with all cart items", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<List<CartItemResponseDTO>>"), examples = {
          @ExampleObject(name = "Success response", ref = "GetAllCartItemsExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<List<CartItemResponseDTO>> getAllCartItems(
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Authenticated user", schema = @Schema(name = "Jwt", type = "object"))
    @AuthenticationPrincipal Jwt jwt
  );

  @Operation(
    operationId = "removeItemFromCart",
    summary = "Remove an item from cart",
    description = "Remove an item from cart",
    responses = {
      @ApiResponse(responseCode = "200", description = "Return a ResponsePayload with a success message", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<Void>"), examples = {
          @ExampleObject(name = "Success response", ref = "RemoveItemFromCartExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<Void> removeItemFromCart(
    @Parameter(in = ParameterIn.PATH, name = "itemId", description = " Item id", schema = @Schema(type = "integer", format = "int64", example = "1"), required = true)
    @PathVariable Long itemId,
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Authenticated user", schema = @Schema(name = "Jwt", type = "object"))
    @AuthenticationPrincipal Jwt jwt
  );

  @Operation(
    operationId = "getCartItemById",
    summary = "Get a cart item by id",
    description = "Get a cart item by id",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with the found cart item", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<CartItemResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "GetCartItemByIdExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<CartItemResponseDTO> getCartItemById(
    @Parameter(in = ParameterIn.PATH, name = "itemId", description = " Item id", schema = @Schema(type = "integer", format = "int64", example = "1"), required = true)
    @PathVariable Long itemId,
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Authenticated user", schema = @Schema(name = "Jwt", type = "object"))
    @AuthenticationPrincipal Jwt jwt
  );

  @Operation(
    operationId = "updateCartItem",
    summary = "Update a cart item",
    description = "Update a cart item",
    requestBody = @RequestBody(description = "Request body to update an item"),
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with the updated item", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<CartItemResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "UpdateCartItemExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<CartItemResponseDTO> updateCartItem(
    @Parameter(in = ParameterIn.PATH, name = "itemId", description = " Item id", schema = @Schema(type = "integer", format = "int64", example = "1"), required = true)
    @PathVariable Long itemId,
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Authenticated user", schema = @Schema(name = "Jwt", type = "object"))
    @AuthenticationPrincipal Jwt jwt,
    @Parameter(name = "UpdateCartItemDTO", required = true)
    @Valid @org.springframework.web.bind.annotation.RequestBody UpdateCartItemDTO itemDTO
  );
}
