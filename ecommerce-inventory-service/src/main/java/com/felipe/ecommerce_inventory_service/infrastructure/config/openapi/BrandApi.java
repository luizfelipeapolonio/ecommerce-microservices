package com.felipe.ecommerce_inventory_service.infrastructure.config.openapi;

import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.BrandDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.CreateOrUpdateBrandDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.validation.OnCreate;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.validation.OnUpdate;
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
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

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
    @Validated(OnCreate.class) @org.springframework.web.bind.annotation.RequestBody CreateOrUpdateBrandDTO brandDTO
  );

  @Operation(
    operationId = "getAllBrands",
    summary = "Get all brands",
    description = "Get a list of all product brands",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with a list of all product brands", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<List<BrandDTO>>"), examples = {
          @ExampleObject(name = "Success response", ref = "GetAllBrandsExample")
        })
      }),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<List<BrandDTO>> getAllBrands();

  @Operation(
    operationId = "getBrandById",
    summary = "Get a brand by id",
    description = "Get a specific product brand by id",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with the found brand", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<BrandDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "GetBrandByIdExample")
        })
      }),
      @ApiResponse(responseCode = "400", description = "Returns an error response if the brand id is zero or is not a positive number", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<List<CustomValidationErrors>>"), examples = {
          @ExampleObject(name = "Error response", ref = "ConstraintViolationExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<BrandDTO> getBrandById(
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Brand id", schema = @Schema(type = "integer", format = "int64"), required = true)
    @Positive(message = "O id da marca não deve ser zero, nem valores negativos") @PathVariable Long id
  );

  @Operation(
    operationId = "updateBrand",
    summary = "Update a brand",
    description = "Update some brand information",
    requestBody = @RequestBody(description = "Request body to update brand information"),
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with the updated brand", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<BrandDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "UpdateBrandExample")
        })
      }),
      @ApiResponse(responseCode = "400", description = "Returns an error response if the brand id is zero or is not a positive number", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<List<CustomValidationErrors>>"), examples = {
          @ExampleObject(name = "Error response", ref = "ConstraintViolationExample")
        })
      }),
      @ApiResponse(responseCode = "409", description = "Returns an error response if the given brand already exists", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<Void>"), examples = {
          @ExampleObject(name = "Error response", ref = "ExistingBrandExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "422", ref = "ValidationErrors"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<BrandDTO> updateBrand(
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Brand id", schema = @Schema(type = "integer", format = "int64"), required = true)
    @Positive(message = "O id da marca não deve ser zero, nem valores negativos") @PathVariable Long id,
    @Validated(OnUpdate.class) @org.springframework.web.bind.annotation.RequestBody CreateOrUpdateBrandDTO brandDTO
  );
}
