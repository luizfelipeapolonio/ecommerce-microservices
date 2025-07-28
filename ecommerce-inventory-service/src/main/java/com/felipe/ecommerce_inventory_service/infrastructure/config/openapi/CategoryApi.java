package com.felipe.ecommerce_inventory_service.infrastructure.config.openapi;

import com.felipe.ecommerce_inventory_service.core.application.dtos.CategoriesDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CategoryDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CreateOrUpdateCategoryDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CreateSubcategoryDTO;
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

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SecurityRequirement(name = OpenAPIConfiguration.BEARER_TOKEN_AUTH)
@Tag(name = "Category")
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
    @Parameter(name = "CreateOrUpdateCategoryDTO", required = true)
    @Valid @org.springframework.web.bind.annotation.RequestBody CreateOrUpdateCategoryDTO createOrUpdateCategoryDTO
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

  @Operation(
    operationId = "updateCategory",
    summary = "Update a category",
    description = "Update a category name",
    requestBody = @RequestBody(description = "Request body to update a category name"),
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with the updatedCategory", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<CategoryDTO>"), examples = {
          @ExampleObject(name = "Success Response", ref = "UpdateCategoryExample")
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
  ResponsePayload<CategoryDTO> updateCategory(
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Category id", schema = @Schema(type = "integer", format = "int64"), required = true)
    @PathVariable Long id,
    @Parameter(name = "CreateOrUpdatedCategoryDTO", required = true)
    @Valid @org.springframework.web.bind.annotation.RequestBody CreateOrUpdateCategoryDTO createOrUpdateCategoryDTO
  );

  @Operation(
    operationId = "getCategoryById",
    summary = "Get a category",
    description = "Get a specific category by id",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with the found category", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<CategoryDTO"), examples = {
          @ExampleObject(name = "Success response", ref = "GetCategoryByIdExample")
        })
      }),
      @ApiResponse(responseCode = "400", description = "Returns an error response if the category id is zero or is not a positive number", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<List<CustomValidationErrors>>"), examples = {
          @ExampleObject(name = "Error response", ref = "ConstraintViolationExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<CategoryDTO> getCategoryById(
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Category id", schema = @Schema(type = "integer", format = "int64"), required = true)
    @Positive(message = "O id da categoria não deve ser zero, nem valores negativos") @PathVariable Long id
  );

  @Operation(
    operationId = "getAllCategories",
    summary = "Get all categories",
    description = "Get a list of all product categories",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with the list of all product categories", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<List<CategoriesDTO>>"), examples = {
          @ExampleObject(name = "Success response", ref = "ListOfCategoriesExample")
        })
      }),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<List<CategoriesDTO>> getAllCategories();
}
