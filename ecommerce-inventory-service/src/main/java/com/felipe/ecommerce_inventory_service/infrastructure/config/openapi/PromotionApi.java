package com.felipe.ecommerce_inventory_service.infrastructure.config.openapi;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PromotionDTO;
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
import org.springframework.http.MediaType;

import java.util.Map;

@SecurityRequirement(name = OpenAPIConfiguration.BEARER_TOKEN_AUTH)
@Tag(name = "Promotion")
public interface PromotionApi {

  @Operation(
    operationId = "applyPromotion",
    summary = "Apply promotion to products",
    description = "Apply promotion to products",
    requestBody = @RequestBody(description = "Request body of promotion to be applied", content = {
      @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
        @ExampleObject(name = "Request body", ref = "PromotionDTOExample")
      })
    }),
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with the quantity of applied promotion", content = {
        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<ApplyPromotionResponse>"), examples = {
          @ExampleObject(name = "Success applied promotion", ref = "AppliedPromotionExample"),
          @ExampleObject(name = "Success no applied promotion", ref = "NoAppliedPromotionExample")
        })
      }),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<Map<String, Integer>> applyPromotion(
    @Parameter(name = "PromotionDTO", required = true)
    @org.springframework.web.bind.annotation.RequestBody PromotionDTO promotionDTO
  );
}
