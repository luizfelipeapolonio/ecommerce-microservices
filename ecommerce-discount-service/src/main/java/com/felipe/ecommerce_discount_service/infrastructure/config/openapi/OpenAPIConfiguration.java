package com.felipe.ecommerce_discount_service.infrastructure.config.openapi;

import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.core.domain.PromotionAppliesTo;
import com.felipe.ecommerce_discount_service.core.domain.enums.DiscountType;
import com.felipe.ecommerce_discount_service.core.domain.enums.PromotionAppliesTarget;
import com.felipe.ecommerce_discount_service.core.domain.enums.PromotionScope;
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
        new Tag().name("Promotion").description("All promotion operations")
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
    };
  }
}
