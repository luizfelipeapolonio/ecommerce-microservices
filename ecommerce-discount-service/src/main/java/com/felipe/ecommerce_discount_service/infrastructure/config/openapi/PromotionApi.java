package com.felipe.ecommerce_discount_service.infrastructure.config.openapi;

import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.CreatePromotionDTOImpl;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.PromotionResponseDTO;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.UpdatePromotionDTOImpl;
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
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SecurityRequirement(name = OpenAPIConfiguration.BEARER_TOKEN_AUTH)
@Tag(name = "Promotion")
public interface PromotionApi {

  @Operation(
    operationId = "createPromotion",
    summary = "Create a promotion",
    description = "Create a promotion",
    requestBody = @RequestBody(description = "Request body to create a promotion"),
    responses = {
      @ApiResponse(responseCode = "201", description = "Returns a ResponsePayload with the created promotion", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<PromotionResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "CreatePromotionResponseExample")
        })
      }),
      @ApiResponse(responseCode = "400", description = "Returns an error response if supplied promotion data is invalid", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<Void>"), examples = {
          @ExampleObject(name = "Error response", ref = "InvalidPromotionDataExample")
        })
      }),
      @ApiResponse(responseCode = "422", ref = "ValidationErrors"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<PromotionResponseDTO> createPromotion(
    @Parameter(name = "CreatePromotionDTO", required = true)
    @Valid @org.springframework.web.bind.annotation.RequestBody CreatePromotionDTOImpl promotionDTO
  );

  @Operation(
    operationId = "getAllPromotions",
    summary = "Get all promotions",
    description = "Get all promotions",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with all promotions", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<List<PromotionResponseDTO>>"), examples = {
          @ExampleObject(name = "Success response", ref = "AllPromotionsExample")
        })
      }),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<List<PromotionResponseDTO>> getAllPromotions();

  @Operation(
    operationId = "deletePromotion",
    summary = "Delete a Promotion",
    description = "Delete a Promotion",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with a message with the deleted promotion name", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<Void>"), examples = {
          @ExampleObject(name = "Success response", ref = "DeletePromotionExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<Void> deletePromotion(
    @Parameter(in = ParameterIn.PATH, name = "promotionId", description = "Promotion id", required = true, schema = @Schema(type = "string", example = "da4dd8a3-a821-4350-9af2-c5b8f3801330"))
    @PathVariable UUID promotionId
  );

  @Operation(
    operationId = "updatePromotion",
    summary = "Update a promotion",
    description = "Update a promotion",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with the updated promotion", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<PromotionResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "UpdatePromotionExample")
        })
      }),
      @ApiResponse(responseCode = "400", description = "Returns an error response if supplied promotion end date is invalid", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<Void>"), examples = {
          @ExampleObject(name = "Error response", ref = "InvalidPromotionEndDateExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "422", ref = "ValidationErrors"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<PromotionResponseDTO> updatePromotion(
    @Parameter(in = ParameterIn.PATH, name = "promotionId", description = "Promotion id", required = true, schema = @Schema(type = "string", example = "da4dd8a3-a821-4350-9af2-c5b8f3801330"))
    @PathVariable UUID promotionId,
    @Valid @RequestBody UpdatePromotionDTOImpl promotionDTO
  );

  @Operation(
    operationId = "getPromotionById",
    summary = "Get a promotion by id",
    description = "Get a promotion by it's id",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with the found promotion", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<PromotionResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "GetPromotionByIdExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<PromotionResponseDTO> getPromotionById(
    @Parameter(in = ParameterIn.PATH, name = "promotionId", description = "Promotion id", required = true, schema = @Schema(type = "string", example = "da4dd8a3-a821-4350-9af2-c5b8f3801330"))
    @PathVariable UUID promotionId
  );

  @Operation(
    operationId = "getAllActiveOrInactivePromotions",
    summary = "Get all active or inactive promotions",
    description = "Get all active or inactive promotions",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with all active or inactive promotions", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<List<PromotionResponseDTO>>"), examples = {
          @ExampleObject(name = "Success response", ref = "AllActiveOrInactivePromotionsExample")
        })
      }),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<List<PromotionResponseDTO>> getAllActiveOrInactivePromotions(
    @Parameter(
      in = ParameterIn.QUERY,
      name = "isActive",
      description = "Boolean parameter to select active or inactive promotions",
      schema = @Schema(type = "boolean", example = "true")
    )
    @RequestParam(value = "isActive", defaultValue = "true") boolean isActive
  );

  @Operation(
    operationId = "getAllPromotionsByDiscountType",
    summary = "Get all promotions by discount type",
    description = "Get all promotions selected by discount type",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with all promotions selected by discount type", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<List<PromotionResponseDTO>>"), examples = {
          @ExampleObject(name = "Success response", ref = "AllPromotionsByDiscountTypeExample")
        })
      }),
      @ApiResponse(responseCode = "422", ref = "ValidationErrors"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<List<PromotionResponseDTO>> getAllPromotionsByDiscountType(
    @Parameter(
      in = ParameterIn.QUERY,
      name = "discountType",
      description = "Parameter with the discount type",
      schema = @Schema(type = "string", example = "percentage")
    )
    @Pattern(regexp = "^(fixed_amount|percentage)$", message = "Tipo de desconto inválido! Os valores aceitos são 'fixed_amount' e 'percentage'")
    @RequestParam(value = "discountType", defaultValue = "percentage") String discountType
  );
}
