package com.felipe.ecommerce_discount_service.infrastructure.config.openapi;

import com.felipe.ecommerce_discount_service.infrastructure.dtos.coupon.CouponResponseDTO;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.coupon.CreateCouponDTOImpl;
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

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SecurityRequirement(name = OpenAPIConfiguration.BEARER_TOKEN_AUTH)
@Tag(name = "Coupon")
public interface CouponApi {

  @Operation(
    operationId = "createCoupon",
    summary = "Create a coupon",
    description = "Create a coupon",
    requestBody = @RequestBody(description = "Request body to create a coupon"),
    responses = {
      @ApiResponse(responseCode = "201", description = "Returns a ResponsePayload with the created coupon", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<CouponResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "CreateCouponExample")
        })
      }),
      @ApiResponse(responseCode = "400", description = "Returns an error response if the supplied coupon end date is invalid", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<Void>"), examples = {
          @ExampleObject(name = "Error response", ref = "InvalidPromotionEndDateExample")
        })
      }),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<CouponResponseDTO> createCoupon(
    @Parameter(name = "CreateCouponDTO", required = true)
    @Valid @org.springframework.web.bind.annotation.RequestBody CreateCouponDTOImpl couponDTO
  );

  @Operation(
    operationId = "checkIfCouponIsValid",
    summary = "Check if the coupon is valid",
    description = "Check if the coupon is valid",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with the coupon data if it is a valid coupon", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<CouponResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "CheckIfCouponIsValidExample")
        })
      }),
      @ApiResponse(responseCode = "422", ref = "ValidationErrors"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<CouponResponseDTO> checkIfCouponIsValid(
    @Parameter(in = ParameterIn.QUERY, name = "couponCode", description = "The coupon code", schema = @Schema(type = "string", example = "COUPON20OFF"))
    @RequestParam(name = "couponCode", required = false) String couponCode
  );

  @Operation(
    operationId = "getAllActiveCoupons",
    summary = "Get all active coupons",
    description = "Get all active coupons",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with a list of all active coupons", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema= @Schema(ref = "ResponsePayload<List<CouponResponseDTO>>"), examples = {
          @ExampleObject(name = "Success response", ref = "AllActiveCouponsExample")
        })
      }),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<List<CouponResponseDTO>> getAllActiveCoupons();

  @Operation(
    operationId = "getAllCoupons",
    summary = "Get all coupons",
    description = "Get all existing coupons",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with a list of all existing coupons", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema= @Schema(ref = "ResponsePayload<List<CouponResponseDTO>>"), examples = {
          @ExampleObject(name = "Success response", ref = "AllCouponsExample")
        })
      }),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<List<CouponResponseDTO>> getAllCoupons();

  @Operation(
    operationId = "getCouponById",
    summary = "Get a coupon by id",
    description = "Get a coupon by id",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with the found coupon data", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<CouponResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "GetCouponByIdExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<CouponResponseDTO> getCouponById(
    @Parameter(in = ParameterIn.PATH, name = "couponId", description = "Coupon id", required = true, schema = @Schema(type = "string", example = "26849051-6958-4c03-b9cf-5e005b5733c9"))
    @PathVariable UUID couponId
  );

  @Operation(
    operationId = "deleteCoupon",
    summary = "Delete a coupon",
    description = "Delete a coupon",
    responses = {
      @ApiResponse(responseCode = "200", description = "Returns a ResponsePayload with the deleted coupon data", content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(ref = "ResponsePayload<CouponResponseDTO>"), examples = {
          @ExampleObject(name = "Success response", ref = "DeleteCouponExample")
        })
      }),
      @ApiResponse(responseCode = "404", ref = "NotFound"),
      @ApiResponse(responseCode = "500", ref = "InternalServerError")
    }
  )
  ResponsePayload<CouponResponseDTO> deleteCoupon(
    @Parameter(in = ParameterIn.PATH, name = "couponId", description = "Coupon id", required = true, schema = @Schema(type = "string", example = "26849051-6958-4c03-b9cf-5e005b5733c9"))
    @PathVariable UUID couponId
  );
}
