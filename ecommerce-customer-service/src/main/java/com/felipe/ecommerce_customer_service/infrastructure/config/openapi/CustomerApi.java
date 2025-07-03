package com.felipe.ecommerce_customer_service.infrastructure.config.openapi;

import com.felipe.ecommerce_customer_service.core.application.dtos.UpdateCustomerDTO;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.AddressDTO;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CreateCustomerDTO;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CustomerDTO;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CustomerProfileDTO;
import com.felipe.response.ResponsePayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface CustomerApi {

  @Operation(
    operationId = "createCustomer",
    summary = "Create a customer",
    description = "Create a customer account",
    requestBody = @RequestBody(description = "Request body to create a customer account"),
    tags = { "Customers" },
    responses = {
      @ApiResponse(responseCode = "201", description = "Returns a ResponsePayload with the created customer account", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(type = "object", ref = "ResponsePayload<CustomerDTO>")),
      }),
      @ApiResponse(responseCode = "409", description = "Returns an error response if the given e-mail already exists", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<Void>"), examples = {
          @ExampleObject(name = "Error response", ref = "EmailConflict")
        })
      }),
      @ApiResponse(responseCode = "422", ref = "ValidationErrors"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<CustomerDTO> create(
    @Parameter(name = "CreateCustomerDTO", required = true)
    @Valid @org.springframework.web.bind.annotation.RequestBody CreateCustomerDTO createCustomerDTO
  );

  @Operation(
    operationId = "getCustomerProfile",
    summary = "Get customer profile",
    description = "Get all details of authenticated customer profile",
    tags = { "Customers" },
    security = { @SecurityRequirement(name = OpenAPIConfiguration.BEARER_TOKEN_AUTH) },
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns all profile information of the authenticated customer", content = {
        @Content(mediaType = "application/json", schema = @Schema(ref = "ResponsePayload<CustomerProfileDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "GetCustomerProfileExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<CustomerProfileDTO> getAuthCustomerProfile(@AuthenticationPrincipal Jwt jwt);

  @Operation(
    operationId = "updateCustomer",
    summary = "Update customer profile",
    description = "Changes some customer profile information",
    tags = { "Customers" },
    security = { @SecurityRequirement(name = OpenAPIConfiguration.BEARER_TOKEN_AUTH) },
    requestBody = @RequestBody(description = "Request body to update customer profile information"),
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns the updated customer profile information", content = {
        @Content(mediaType = "application/json", schema = @Schema(ref = "ResponsePayload<CustomerProfileDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "UpdateCustomerExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<CustomerProfileDTO> updateCustomerProfile(
    @AuthenticationPrincipal Jwt jwt,
    @Parameter(name = "UpdateCustomerDTO")
    @org.springframework.web.bind.annotation.RequestBody UpdateCustomerDTO customerDTO
  );

  @Operation(
    operationId = "insertCustomerAddress",
    summary = "Insert customer address",
    description = "Inserts address to the authenticated customer",
    requestBody = @RequestBody(description = "Request body to insert customer address", required = true, content = {
      @Content(mediaType = "application/json", schema = @Schema(ref = "AddressDTO"), examples = {
        @ExampleObject(name = "Request body", ref = "InsertAddressDTOExample")
      })
    }),
    tags = { "Customers" },
    security = { @SecurityRequirement(name = OpenAPIConfiguration.BEARER_TOKEN_AUTH) },
    responses = {
      @ApiResponse(responseCode = "201", description = "Returns the customer profile with the inserted address", content = {
        @Content(mediaType = "application/json", schema = @Schema(ref = "ResponsePayload<CustomerProfileDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "InsertAddressExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "422", ref = "ValidationErrors"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<CustomerProfileDTO> insertAddress(
    @AuthenticationPrincipal Jwt jwt,
    @Parameter(name = "AddressDTO", required = true)
    @Valid @org.springframework.web.bind.annotation.RequestBody AddressDTO addressDTO
  );

  @Operation(
    operationId = "updateCustomerAddress",
    summary = "Update customer address",
    description = "Changes customer address",
    requestBody = @RequestBody(description = "Request body to update customer address", content = {
      @Content(mediaType = "application/json", schema = @Schema(ref = "UpdateAddressDTO"), examples = {
        @ExampleObject(name = "Request body", ref = "InsertAddressDTOExample")
      })
    }),
    tags = { "Customers" },
    security = { @SecurityRequirement(name = OpenAPIConfiguration.BEARER_TOKEN_AUTH) },
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns the customer profile with the updated address", content = {
        @Content(mediaType = "application/json", schema = @Schema(ref = "ResponsePayload<CustomerProfileDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "UpdateAddressExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<CustomerProfileDTO> updateAddress(
    @AuthenticationPrincipal Jwt jwt,
    @Parameter(name = "UpdateAddressDTO", required = true)
    @org.springframework.web.bind.annotation.RequestBody AddressDTO addressDTO
  );
}
