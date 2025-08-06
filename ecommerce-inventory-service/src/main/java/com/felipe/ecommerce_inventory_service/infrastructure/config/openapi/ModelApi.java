package com.felipe.ecommerce_inventory_service.infrastructure.config.openapi;

import com.felipe.ecommerce_inventory_service.infrastructure.dtos.model.CreateModelDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.model.ModelDTO;
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
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SecurityRequirement(name = OpenAPIConfiguration.BEARER_TOKEN_AUTH)
@Tag(name = "Model")
public interface ModelApi {

  @Operation(
    operationId = "createModel",
    summary = "Create a model",
    description = "Create a product model",
    requestBody = @RequestBody(description = "Request body to create a model"),
    responses = {
      @ApiResponse(responseCode = "201", description = "Returns a ResponsePayload with the created model", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<ModelDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "CreateModelExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "409", description = "Returns an error response if the given model already exists", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<Void>"), examples = {
          @ExampleObject(name = "Error response", ref = "ExistingModelExample")
        })
      }),
      @ApiResponse(responseCode = "422", ref = "ValidationErrors"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<ModelDTO> createModel(
    @Parameter(name = "CreateModelDTO", required = true)
    @Valid @org.springframework.web.bind.annotation.RequestBody CreateModelDTO modelDTO
  );

  @Operation(
    operationId = "getAllModelsOfBrand",
    summary = "Get all models of brand",
    description = "Get all the product models of a specific brand",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with a list of all product models", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<List<ModelDTO>>"), examples = {
          @ExampleObject(name = "Success response", ref = "AllModelsOfBrandExample")
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
  ResponsePayload<List<ModelDTO>> getAllModelsOfBrand(
    @Parameter(in = ParameterIn.QUERY, name = "brandId", description = "Brand id", schema = @Schema(type = "integer", format = "int64", example = "1"), required = true)
    @Positive(message = "O id da marca não deve ser zero, nem valores negativos")
    @RequestParam("brandId") Long brandId
  );

  @Operation(
    operationId = "getModelById",
    summary = "Get a model by id",
    description = "Get a specific product model by id",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with a specific product model", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<ModelDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "GetModelByIdExample")
        })
      }),
      @ApiResponse(responseCode = "400", description = "Returns an error response if the model id is zero or is not a positive number", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<List<CustomValidationErrors>>"), examples = {
          @ExampleObject(name = "Error response", ref = "ConstraintViolationExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<ModelDTO> getModelById(
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Model id", schema = @Schema(type = "integer", format = "int64", example = "1"), required = true)
    @Positive(message = "O id do modelo não deve ser zero, nem valores negativos")
    @PathVariable Long id
  );
}
