package com.felipe.ecommerce_inventory_service.infrastructure.config.openapi;

import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CategoryDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CreateCategoryDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CreateSubcategoryDTO;
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
@Tag(name = "Inventory")
public interface CategoryApi {

  @Operation(
    operationId = "createCategory",
    summary = "Create a category",
    description = "Create a product category",
    requestBody = @RequestBody(description = "Request body to create a product category"),
    responses = {
      @ApiResponse(responseCode = "201", description = "Returns a ResponsePayload with the created product category", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<CategoryDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "CategoryDTOWithNoSubcategoryExample")
        })
      }),
      @ApiResponse(responseCode = "409", description = "Returns an error response if the given category already exists", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<Void>"), examples = {
          @ExampleObject(name = "Error response", ref = "ExistingCategoryExample")
        })
      }),
      @ApiResponse(responseCode = "422", ref = "ValidationErrors"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<CategoryDTO> createCategory(
    @Parameter(name = "CreateCategoryDTO", required = true)
    @Valid @org.springframework.web.bind.annotation.RequestBody CreateCategoryDTO createCategoryDTO
  );

  @Operation(
    operationId = "createSubcategory",
    summary = "Create a subcategory",
    description = "Create a subcategory of product category",
    requestBody = @RequestBody(description = "Request body to create a product subcategory"),
    responses = {
      @ApiResponse(responseCode = "201", description = "Returns a ResponsePayload with the created subcategory", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<CategoryDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "CreateSubcategoryExample")
        })
      }),
      @ApiResponse(responseCode = "409", description = "Returns an error response if the given category name already exists", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<Void>"), examples = {
          @ExampleObject(name = "Error response", ref = "ExistingCategoryExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "422", ref = "ValidationErrors"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<CategoryDTO> createSubcategory(
    @Parameter(name = "CreateSubcategoryDTO", required = true)
    @Valid @org.springframework.web.bind.annotation.RequestBody CreateSubcategoryDTO createSubcategoryDTO
  );
}
