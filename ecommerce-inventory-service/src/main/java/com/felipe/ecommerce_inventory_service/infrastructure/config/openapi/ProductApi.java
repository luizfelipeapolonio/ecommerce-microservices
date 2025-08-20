package com.felipe.ecommerce_inventory_service.infrastructure.config.openapi;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.CreateProductResponseDTO;
import com.felipe.response.ResponsePayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

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
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<CreateProductResponseDTO>"), examples = {
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
  ResponsePayload<CreateProductResponseDTO> createProduct(
    @RequestPart("productDTO") String jsonProductDTO,
    @RequestPart("images") MultipartFile[] images
  );
}
