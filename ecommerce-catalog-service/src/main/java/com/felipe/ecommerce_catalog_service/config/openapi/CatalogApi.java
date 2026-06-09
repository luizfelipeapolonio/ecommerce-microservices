package com.felipe.ecommerce_catalog_service.config.openapi;

import com.felipe.ecommerce_catalog_service.dtos.HomepageProductsDTO;
import com.felipe.response.ResponsePayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SecurityRequirement(name = OpenAPIConfiguration.BEARER_TOKEN_AUTH)
@Tag(name = "Catalog")
public interface CatalogApi {

  @Operation(
    operationId = "getHomepageProducts",
    summary = "Get the homepage products",
    description = "Get the homepage products",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with the homepage catalog", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<HomepageProductsDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "GetHomepageProductsExample")
        })
      }),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<HomepageProductsDTO> getHomepageProducts();
}
