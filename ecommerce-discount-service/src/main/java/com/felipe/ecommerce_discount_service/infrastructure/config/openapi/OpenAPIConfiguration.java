package com.felipe.ecommerce_discount_service.infrastructure.config.openapi;

import com.felipe.ecommerce_discount_service.core.domain.Coupon;
import com.felipe.ecommerce_discount_service.core.domain.CouponAppliedBy;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.core.domain.PromotionAppliesTo;
import com.felipe.ecommerce_discount_service.core.domain.enums.DiscountType;
import com.felipe.ecommerce_discount_service.core.domain.enums.PromotionAppliesTarget;
import com.felipe.ecommerce_discount_service.core.domain.enums.PromotionScope;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.coupon.CouponAppliedByDTO;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.coupon.CouponResponseDTO;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.coupon.CreateCouponDTOImpl;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.PromotionAppliesToResponseDTO;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.PromotionResponseDTO;
import com.felipe.openapi.OpenApiUtils;
import com.felipe.openapi.SchemaCustomizer;
import com.felipe.response.ResponseType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.felipe.openapi.OpenApiUtils.SCHEMAS_REF;

@OpenAPIDefinition
@Configuration
public class OpenAPIConfiguration {
  private final OpenApiUtils apiUtils = OpenApiUtils.getInstanceWithCommonSchemas();
  public static final String BEARER_TOKEN_AUTH = "bearer_token_auth";

  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
      .servers(List.of(new Server().url("http://localhost:8083")))
      .info(new Info()
        .title("Discount Service API")
        .description("This lists all the Discount Service API calls.")
        .version("1.0.0"))
      .tags(List.of(
        new Tag().name("Promotion").description("All promotion operations"),
        new Tag().name("Coupon").description("All coupon operations")
      ))
      .components(new Components()
        .schemas(this.apiUtils.getSchemas())
        .responses(this.apiUtils.getResponses())
        .examples(this.apiUtils.getExamples())
        .addSecuritySchemes(BEARER_TOKEN_AUTH, new SecurityScheme()
          .name(BEARER_TOKEN_AUTH)
          .scheme("bearer")
          .type(SecurityScheme.Type.HTTP)
          .bearerFormat("JWT")));
  }

  @Bean
  CommandLineRunner insertComponents() {
    return args -> {
      final ModelConverters modelConverterInstance = ModelConverters.getInstance();
      this.apiUtils.createSchemaFromClass(
        "PromotionAppliesToResponseDTO",
        modelConverterInstance,
        PromotionAppliesToResponseDTO.class,
        SchemaCustomizer.withDefaults()
      );
      this.apiUtils.createSchemaFromClass(
        "PromotionResponseDTO",
        modelConverterInstance,
        PromotionResponseDTO.class,
        SchemaCustomizer.withDefaults()
      );
      this.apiUtils.createSchemaFromClass(
        "CreateCouponDTO",
        modelConverterInstance,
        CreateCouponDTOImpl.class,
        SchemaCustomizer.withDefaults()
      );
      this.apiUtils.createSchemaFromClass(
        "CouponResponseDTO",
        modelConverterInstance,
        CouponResponseDTO.class,
        SchemaCustomizer.withDefaults()
      );
      this.apiUtils.createSchemaFromClass(
        "CouponAppliedByDTO",
        modelConverterInstance,
        CouponAppliedByDTO.class,
        SchemaCustomizer.withDefaults()
      );
      this.apiUtils.createSchema("ResponsePayload<PromotionResponseDTO>", schema -> {
        schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
        schema.addAllOfItem(new ObjectSchema()
          .addProperty("payload", new ObjectSchema().$ref(SCHEMAS_REF + "PromotionResponseDTO")));
      });
      this.apiUtils.createSchema("ResponsePayload<List<PromotionResponseDTO>>", schema -> {
        schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
        schema.addAllOfItem(new ObjectSchema().addProperty("payload", new ArraySchema()
          .items(new ObjectSchema().$ref(SCHEMAS_REF + "PromotionResponseDTO"))));
      });
      this.apiUtils.createSchema("ResponsePayload<CouponResponseDTO>", schema -> {
        schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
        schema.addAllOfItem(new ObjectSchema()
          .addProperty("payload", new ObjectSchema().$ref(SCHEMAS_REF + "CouponResponseDTO")));
      });
      this.apiUtils.createSchema("ResponsePayload<List<CouponResponseDTO>>", schema -> {
        schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
        schema.addAllOfItem(new ObjectSchema().addProperty("payload", new ArraySchema()
          .items(new ObjectSchema().$ref(SCHEMAS_REF + "CouponResponseDTO"))));
      });

      // Examples
      PromotionAppliesTo target1 = new PromotionAppliesTo();
      target1.setId(1L);
      target1.setTarget(PromotionAppliesTarget.CATEGORY);
      target1.setTargetId("1");
      target1.setAppliedAt(LocalDateTime.parse("2025-10-16T20:43:52.051157306"));

      PromotionAppliesTo target2 = new PromotionAppliesTo();
      target2.setId(2L);
      target2.setTarget(PromotionAppliesTarget.BRAND);
      target2.setTargetId("1");
      target2.setAppliedAt(LocalDateTime.parse("2025-10-16T20:43:52.051157306"));

      Promotion promotion1 = Promotion.builder()
        .id(UUID.fromString("460d389c-9128-4fb1-85e3-b4647a3aa4c0"))
        .name("50% OFF")
        .description("50% off discount")
        .scope(PromotionScope.ALL)
        .discountType(DiscountType.PERCENTAGE)
        .discountValue("50.00")
        .minimumPrice(new BigDecimal("120.00"))
        .endDate(LocalDateTime.parse("2026-10-16T20:43:52.051157306"))
        .createdAt(LocalDateTime.parse("2025-10-16T20:43:52.051157306"))
        .updatedAt(LocalDateTime.parse("2025-10-16T20:43:52.051157306"))
        .promotionApplies(List.of(target1, target2))
        .build();

      Coupon coupon1 = new Coupon()
        .id(UUID.fromString("26849051-6958-4c03-b9cf-5e005b5733c9"))
        .name("Coupon 1")
        .couponCode("20%OFF")
        .description("Description of Coupon 1")
        .discountType(DiscountType.PERCENTAGE)
        .discountValue("20.00")
        .usageCount(0)
        .usageLimit(10)
        .minimumPrice(new BigDecimal("100.00"))
        .createdAt(LocalDateTime.parse("2026-05-25T14:49:35.174364200"))
        .updatedAt(LocalDateTime.parse("2026-05-25T14:49:35.174364200"))
        .endDate(LocalDateTime.parse("2027-05-25T14:49:35.174364200"));

      CouponAppliedBy appliedBy1 = new CouponAppliedBy()
        .id(1L)
        .orderId(UUID.fromString("31d0ccbc-7b4e-4d2e-9921-b109c4729888"))
        .customerId(UUID.fromString("0bab9c97-2d08-420a-8e70-9114da95ad4d"))
        .appliedAt(LocalDateTime.parse("2026-05-25T14:49:35.174364200"));

      coupon1.addAppliedBy(appliedBy1);
      CouponResponseDTO couponResponseDTO = new CouponResponseDTO(coupon1);

      this.apiUtils.createExample(
        "CreatePromotionResponseExample",
        ResponseType.SUCCESS,
        HttpStatus.CREATED,
        "Promotion applied successfully",
        new PromotionResponseDTO(promotion1)
      );
      this.apiUtils.createExample(
        "InvalidPromotionDataExample",
        ResponseType.ERROR,
        HttpStatus.BAD_REQUEST,
        "Invalid discount type! The value 'amount' is not a valid discount type",
        null
      );
      this.apiUtils.createExample(
        "InvalidPromotionEndDateExample",
        ResponseType.ERROR,
        HttpStatus.BAD_REQUEST,
        "Invalid end date! The end date should not be before the current date. Invalid end date: 2025-05-12T13:00",
        null
      );
      this.apiUtils.createExample(
        "DeletePromotionExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Promotion '50% OFF' deleted successfully",
        null
      );
      this.apiUtils.createExample(
        "UpdatePromotionExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Promotion updated successfully",
        new PromotionResponseDTO(promotion1)
      );
      this.apiUtils.createExample(
        "GetPromotionByIdExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Promotion found with id: 'da4dd8a3-a821-4350-9af2-c5b8f3801330'",
        new PromotionResponseDTO(promotion1)
      );
      this.apiUtils.createExample(
        "AllPromotionsExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "All promotions",
        List.of(new PromotionResponseDTO(promotion1))
      );
      this.apiUtils.createExample(
        "AllActiveOrInactivePromotionsExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "All active promotions found",
        List.of(new PromotionResponseDTO(promotion1))
      );
      this.apiUtils.createExample(
        "AllPromotionsByDiscountTypeExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "All promotions with discount type 'percentage'",
        List.of(new PromotionResponseDTO(promotion1))
      );

      this.apiUtils.createExample(
        "CreateCouponExample",
        ResponseType.SUCCESS,
        HttpStatus.CREATED,
        "Coupon created successfully",
        couponResponseDTO
      );
      this.apiUtils.createExample(
        "CheckIfCouponIsValidExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Coupon '20%OFF'",
        couponResponseDTO
      );
      this.apiUtils.createExample(
        "AllActiveCouponsExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "All active coupons",
        List.of(couponResponseDTO)
      );
      this.apiUtils.createExample(
        "AllCouponsExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "All coupons",
        List.of(couponResponseDTO)
      );
      this.apiUtils.createExample(
        "GetCouponByIdExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Coupon with id '26849051-6958-4c03-b9cf-5e005b5733c9' found",
        couponResponseDTO
      );
      this.apiUtils.createExample(
        "DeleteCouponExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Coupon with id '26849051-6958-4c03-b9cf-5e005b5733c9' deleted successfully",
        couponResponseDTO
      );
    };
  }
}
