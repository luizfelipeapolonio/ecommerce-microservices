package com.felipe.ecommerce_inventory_service.infrastructure.config.openapi;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PageResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ProductResponseDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.product.StockProductQuantityDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.product.StockProductQuantityResponseDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.product.UpdateProductDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.product.UpdateProductResponseDTO;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@SecurityRequirement(name = OpenAPIConfiguration.BEARER_TOKEN_AUTH)
@Tag(name = "Product")
public interface ProductApi {

  @Operation(
    operationId = "createProduct",
    summary = "Create a product",
    description = "Create a product",
    requestBody = @RequestBody(
      description = "Request body to create a product. The product data should be sent in a form data property named \"productDTO\" " +
      "with the following properties as JSON string: ", content = {
        @Content(mediaType = MULTIPART_FORM_DATA_VALUE, schema = @Schema(name = "productDTO", ref = "CreateProductDTO"))
    }),
    responses = {
      @ApiResponse(responseCode = "201", description = "Returns a ResponsePayload with the created product", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<ProductDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "CreateProductExample")
        })
      }),
      @ApiResponse(responseCode = "400", description = "Returns an error response with the fields validation errors", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<List<CustomValidationErrors>>"), examples = {
          @ExampleObject(name = "Error response", ref = "CreateProductViolationExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "409", description = "Returns an error response if the given product already exists", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<Void>"), examples = {
          @ExampleObject(name = "Error response", ref = "ExistingProductExample")
        })
      }),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<ProductResponseDTO> createProduct(
    @RequestPart("productDTO") String jsonProductDTO,
    @RequestPart("images") MultipartFile[] images
  );

  @Operation(
    operationId = "updateProduct",
    summary = "Update a product",
    description = "Update some product information",
    requestBody = @RequestBody(description = "Request body to update product information"),
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with the updated product", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<UpdateProductResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "UpdateProductExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "409", description = "Returns an error response if the given product already exists", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<Void>"), examples = {
          @ExampleObject(name = "Error response", ref = "ExistingProductExample")
        })
      }),
      @ApiResponse(responseCode = "422", ref = "ValidationErrors"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<UpdateProductResponseDTO> updateProduct(
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Product id", required = true, schema = @Schema(type = "string", example = "da4dd8a3-a821-4350-9af2-c5b8f3801330"))
    @PathVariable UUID id,
    @Parameter(name = "UpdateProductDTO", required = true)
    @Valid @org.springframework.web.bind.annotation.RequestBody UpdateProductDTO productDTO
  );

  @Operation(
    operationId = "getProductById",
    summary = "Get a product by id",
    description = "Get a product with the given id",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with a product", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<ProductDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "GetProductByIdExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<ProductResponseDTO> getProductById(
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Product id", required = true, schema = @Schema(type = "string", example = "da4dd8a3-a821-4350-9af2-c5b8f3801330"))
    @PathVariable UUID id
  );

  @Operation(
    operationId = "deleteProduct",
    summary = "Delete a product",
    description = "Delete a product",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with a success message", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<Void>"), examples = {
          @ExampleObject(name = "Success response", ref = "DeleteProductExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<Void> deleteProduct(
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Product id", required = true, schema = @Schema(type = "string", example = "da4dd8a3-a821-4350-9af2-c5b8f3801330"))
    @PathVariable UUID id
  );

  @Operation(
    operationId = "checkIfProductIsInStock",
    summary = "Check if a product is in stock",
    description = "Check if there are product units in the stock",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with the product status", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<IsInStockDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "IsProductInStockExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<Map<String, Boolean>> checkIfProductIsInStock(
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Product id", required = true, schema = @Schema(type = "string", example = "da4dd8a3-a821-4350-9af2-c5b8f3801330"))
    @PathVariable UUID id
  );

  @Operation(
    operationId = "addProductInStock",
    summary = "Add product in stock",
    description = "Increase the product quantity in stock",
    requestBody = @RequestBody(description = "Request body to send the product quantity to add in stock"),
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with the current product quantity", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<StockProductQuantityResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "AddProductInStockExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<StockProductQuantityResponseDTO> addProductInStock(
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Product id", required = true, schema = @Schema(type = "string", example = "da4dd8a3-a821-4350-9af2-c5b8f3801330"))
    @PathVariable UUID id,
    @Parameter(name = "StockProductQuantityDTO", required = true)
    @Valid @org.springframework.web.bind.annotation.RequestBody StockProductQuantityDTO productDTO
  );

  @Operation(
    operationId = "removeProductFromStock",
    summary = "Remove product from stock",
    description = "Decrease the product quantity in stock",
    requestBody = @RequestBody(description = "Request body to send the product quantity to remove from stock"),
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with the current product quantity", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<StockProductQuantityResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "RemoveProductFromStockExample")
        })
      }),
      @ApiResponse(responseCode = "400", description = "Returns an error response if the product quantity is invalid", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<Void>"), examples = {
          @ExampleObject(name = "Error response", ref = "InvalidProductQuantityExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<StockProductQuantityResponseDTO> removeProductFromStock(
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Product id", required = true, schema = @Schema(type = "string", example = "da4dd8a3-a821-4350-9af2-c5b8f3801330"))
    @PathVariable UUID id,
    @Parameter(name = "StockProductQuantityDTO", required = true)
    @Valid @org.springframework.web.bind.annotation.RequestBody StockProductQuantityDTO productDTO
  );

  @Operation(
    operationId = "getProducts",
    summary = "Get products by parameters",
    description = "Get products page of the given category, brand and model",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with a page of products", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<ProductPageResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "GetProductsExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<PageResponseDTO> getProducts(
    @Parameter(in = ParameterIn.QUERY, name = "category", description = "The product category name", schema = @Schema(type = "string", example = "mouse"))
    @RequestParam(name = "category") String categoryName,
    @Parameter(in = ParameterIn.QUERY, name = "brand", description = "The product brand name", schema = @Schema(type = "string", example = "logitech"))
    @RequestParam(name = "brand") String brandName,
    @Parameter(in = ParameterIn.QUERY, name = "model", description = "The product model name", schema = @Schema(type = "string", example = "g pro"))
    @RequestParam(name = "model") String modelName,
    @Parameter(in = ParameterIn.QUERY, name = "page", description = "The page number", schema = @Schema(type = "integer", format = "int32", example = "1"))
    @RequestParam(name = "page") int page,
    @Parameter(in = ParameterIn.QUERY, name = "pageSize", description = "The number of elements on the page", schema = @Schema(type = "integer", format = "int32", example = "10"))
    @RequestParam(name = "pageSize") int size
  );

  @Operation(
    operationId = "getAllProducts",
    summary = "Get all products",
    description = "Get all products page",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with a page of products", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<ProductPageResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "GetAllProductsExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<PageResponseDTO> getAllProducts(
    @Parameter(in = ParameterIn.QUERY, name = "page", description = "The page number", schema = @Schema(type = "integer", format = "int32", example = "1"))
    @RequestParam(name = "page") int page,
    @Parameter(in = ParameterIn.QUERY, name = "pageSize", description = "The number of elements on the page", schema = @Schema(type = "integer", format = "int32", example = "10"))
    @RequestParam(name = "pageSize") int size
  );

  @Operation(
    operationId = "getProductsByCategory",
    summary = "Get products by category",
    description = "Get products page of the given category",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with a page of products", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<ProductPageResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "GetProductsByCategoryExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<PageResponseDTO> getProductsByCategory(
    @Parameter(in = ParameterIn.PATH, name = "categoryName", description = "Category name", required = true, schema = @Schema(type = "string", example = "mouse"))
    @PathVariable String categoryName,
    @Parameter(in = ParameterIn.QUERY, name = "page", description = "The page number", schema = @Schema(type = "integer", format = "int32", example = "1"))
    @RequestParam(name = "page") int page,
    @Parameter(in = ParameterIn.QUERY, name = "pageSize", description = "The number of elements on the page", schema = @Schema(type = "integer", format = "int32", example = "10"))
    @RequestParam(name = "pageSize") int size
  );

  @Operation(
    operationId = "getProductsByBrand",
    summary = "Get products by brand",
    description = "Get products page of the given brand",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with a page of products", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<ProductPageResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "GetProductsByBrandExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<PageResponseDTO> getProductsByBrand(
    @Parameter(in = ParameterIn.PATH, name = "brandName", description = "Brand name", required = true, schema = @Schema(type = "string", example = "logitech"))
    @PathVariable String brandName,
    @Parameter(in = ParameterIn.QUERY, name = "page", description = "The page number", schema = @Schema(type = "integer", format = "int32", example = "1"))
    @RequestParam(name = "page") int page,
    @Parameter(in = ParameterIn.QUERY, name = "pageSize", description = "The number of elements on the page", schema = @Schema(type = "integer", format = "int32", example = "10"))
    @RequestParam(name = "pageSize") int size
  );

  @Operation(
    operationId = "getProductsByModel",
    summary = "Get products by model",
    description = "Get products page of the given model and brand",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with a page of products", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<ProductPageResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "GetProductsByModelExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<PageResponseDTO> getProductsByModel(
    @Parameter(in = ParameterIn.PATH, name = "modelName", description = "Model name", required = true, schema = @Schema(type = "string", example = "g pro"))
    @PathVariable String modelName,
    @Parameter(in = ParameterIn.PATH, name = "brandName", description = "Brand name", required = true, schema = @Schema(type = "string", example = "logitech"))
    @PathVariable String brandName,
    @Parameter(in = ParameterIn.QUERY, name = "page", description = "The page number", schema = @Schema(type = "integer", format = "int32", example = "1"))
    @RequestParam(name = "page") int page,
    @Parameter(in = ParameterIn.QUERY, name = "pageSize", description = "The number of elements on the page", schema = @Schema(type = "integer", format = "int32", example = "10"))
    @RequestParam(name = "pageSize") int size
  );
}
