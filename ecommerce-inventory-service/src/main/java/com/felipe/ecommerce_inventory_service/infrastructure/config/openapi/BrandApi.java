package com.felipe.ecommerce_inventory_service.infrastructure.config.openapi;

import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.BrandDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.CreateOrUpdateBrandDTO;
import com.felipe.response.ResponsePayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SecurityRequirement(name = OpenAPIConfiguration.BEARER_TOKEN_AUTH)
@Tag(name = "Brand")
public interface BrandApi {

  @Operation(
    operationId = "createBrand",
    summary = "Create a brand",
    description = "Create a product brand",
    requestBody = @RequestBody(description = "Request body to create a product brand"),
    responses = {
      @ApiResponse(responseCode = "201", description = "Returns a ResponsePayload with the created product brand", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<BrandDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "CreateBrandExample")
        })
      }),
      @ApiResponse(responseCode = "409", description = "Returns an error response if the given brand already exists", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<Void>"), examples = {
          @ExampleObject(name = "Error response", ref = "ExistingBrandExample")
        })
      }),
      @ApiResponse(responseCode = "422", ref = "ValidationErrors"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<BrandDTO> createBrand(
    @Parameter(name = "CreateOrUpdateBrandDTO", required = true)
    @Valid @org.springframework.web.bind.annotation.RequestBody CreateOrUpdateBrandDTO brandDTO
  );
}
