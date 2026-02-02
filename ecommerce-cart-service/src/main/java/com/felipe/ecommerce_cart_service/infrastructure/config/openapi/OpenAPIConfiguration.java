package com.felipe.ecommerce_cart_service.infrastructure.config.openapi;

import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.core.domain.CartItem;
import com.felipe.ecommerce_cart_service.infrastructure.dtos.cart.AddItemToCartDTO;
import com.felipe.ecommerce_cart_service.infrastructure.dtos.cart.CartItemResponseDTO;
import com.felipe.ecommerce_cart_service.infrastructure.dtos.cart.CartResponseDTO;
import com.felipe.ecommerce_cart_service.infrastructure.dtos.cart.CreateCartDTO;
import com.felipe.ecommerce_cart_service.infrastructure.dtos.cart.UpdateCartItemDTO;
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
  private final OpenApiUtils openApiUtils = OpenApiUtils.getInstanceWithCommonSchemas();
  static final String BEARER_TOKEN_AUTH = "bearer_token_auth";
  private static final LocalDateTime MOCK_DATETIME = LocalDateTime.parse("2025-07-18T21:12:28.978228256");

  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
      .servers(List.of(new Server().url("http://localhost:8084")))
      .info(new Info()
        .title("Cart Service API")
        .description("This lists all the Cart Service API calls")
        .version("1.0.0"))
      .tags(List.of(new Tag().name("Cart").description("All cart operations")))
      .components(new Components()
        .schemas(this.openApiUtils.getSchemas())
        .responses(this.openApiUtils.getResponses())
        .examples(this.openApiUtils.getExamples())
        .addSecuritySchemes(BEARER_TOKEN_AUTH, new SecurityScheme()
          .name(BEARER_TOKEN_AUTH)
          .scheme("bearer")
          .type(SecurityScheme.Type.HTTP)
          .bearerFormat("JWT")));
  }

  @Bean
  CommandLineRunner insertComponents() {
    return args -> {
      insertSchemas();
      insertExamples();
    };
  }

  private void insertSchemas() {
    ModelConverters modelConvertersInstance = ModelConverters.getInstance();
    this.openApiUtils.createSchemaFromClass(
      "CreateCartDTO",
      modelConvertersInstance,
      CreateCartDTO.class,
      SchemaCustomizer.withDefaults()
    );
    this.openApiUtils.createSchemaFromClass(
      "CartResponseDTO",
      modelConvertersInstance,
      CartResponseDTO.class,
      SchemaCustomizer.withDefaults()
    );
    this.openApiUtils.createSchemaFromClass(
      "CartItemResponseDTO",
      modelConvertersInstance,
      CartItemResponseDTO.class,
      SchemaCustomizer.withDefaults()
    );
    this.openApiUtils.createSchemaFromClass(
      "AddItemToCartDTO",
      modelConvertersInstance,
      AddItemToCartDTO.class,
      SchemaCustomizer.withDefaults()
    );
    this.openApiUtils.createSchemaFromClass(
      "UpdateCartItemDTO",
      modelConvertersInstance,
      UpdateCartItemDTO.class,
      SchemaCustomizer.withDefaults()
    );
    this.openApiUtils.createSchema("ResponsePayload<CartResponseDTO>", schema -> {
      schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
      schema.addAllOfItem(new ObjectSchema()
        .addProperty("payload", new ObjectSchema().$ref(SCHEMAS_REF + "CartResponseDTO")));
    });
    this.openApiUtils.createSchema("ResponsePayload<CartItemResponseDTO>", schema -> {
      schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
      schema.addAllOfItem(new ObjectSchema()
        .addProperty("payload", new ObjectSchema().$ref(SCHEMAS_REF + "CartItemResponseDTO")));
    });
    this.openApiUtils.createSchema("ResponsePayload<List<CartItemResponseDTO>>", schema -> {
      schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
      schema.addAllOfItem(new ObjectSchema().addProperty("payload", new ArraySchema()
        .items(new ObjectSchema().$ref(SCHEMAS_REF + "CartItemResponseDTO"))));
    });
  }

  private void insertExamples() {
    Cart cart = new Cart();
    cart.setId(UUID.fromString("b8dc2e49-20cd-42d2-9241-1d0535241688"));
    cart.setCustomerId(UUID.fromString("da4dd8a3-a821-4350-9af2-c5b8f3801330"));
    cart.setCreatedAt(MOCK_DATETIME);

    CartItem item1 = new CartItem()
      .id(1L)
      .productId(UUID.fromString("ee2ec142-f3b5-494a-9857-b67abf606f9c"))
      .productName("Product 1")
      .thumbnailImage("/image-product1.jpg")
      .unitPrice(new BigDecimal("120.00"))
      .discountType("fixed_amount")
      .discountValue("20.00")
      .quantity(1)
      .finalPrice(new BigDecimal("100.00"))
      .addedAt(MOCK_DATETIME)
      .cart(cart);
    CartItem item2 = new CartItem()
      .id(2L)
      .productId(UUID.fromString("35c21c70-078e-4f35-9dc7-fec94c6a2723"))
      .productName("Product 2")
      .thumbnailImage("/image-product2.jpg")
      .unitPrice(new BigDecimal("150.00"))
      .discountType("fixed_amount")
      .discountValue("20.00")
      .quantity(1)
      .finalPrice(new BigDecimal("130.00"))
      .addedAt(MOCK_DATETIME)
      .cart(cart);

    CartResponseDTO cartResponseDTO = new CartResponseDTO(cart);
    CartItemResponseDTO cartItemResponseDTO1 = new CartItemResponseDTO(item1);
    CartItemResponseDTO cartItemResponseDTO2 = new CartItemResponseDTO(item2);

    this.openApiUtils.createExample(
      "CreateCartResponseExample",
      ResponseType.SUCCESS,
      HttpStatus.CREATED,
      "Cart of customer with id 'da4dd8a3-a821-4350-9af2-c5b8f3801330' created successfully",
      cartResponseDTO
    );
    this.openApiUtils.createExample(
      "AddItemToCartExample",
      ResponseType.SUCCESS,
      HttpStatus.CREATED,
      "Item '" + item1.getProductName() + "' added successfully in the cart with id '" + cart.getId() + "'",
      cartItemResponseDTO1
    );
    this.openApiUtils.createExample(
      "ExistingCartItemExample",
      ResponseType.ERROR,
      HttpStatus.CONFLICT,
      "The product with id '" + item1.getProductId() + "' is already in the cart",
      null
    );
    this.openApiUtils.createExample(
      "GetAllCartItemsExample",
      ResponseType.SUCCESS,
      HttpStatus.OK,
      "All cart items",
      List.of(cartItemResponseDTO1, cartItemResponseDTO2)
    );
    this.openApiUtils.createExample(
      "RemoveItemFromCartExample",
      ResponseType.SUCCESS,
      HttpStatus.OK,
      "Item 'Product 1' removed successfully",
      null
    );
    this.openApiUtils.createExample(
      "GetCartItemByIdExample",
      ResponseType.SUCCESS,
      HttpStatus.OK,
      "Item with id '1' found",
      cartItemResponseDTO1
    );
    this.openApiUtils.createExample(
      "UpdateCartItemExample",
      ResponseType.SUCCESS,
      HttpStatus.OK,
      "Item with id '1' updated successfully",
      cartItemResponseDTO1
    );
  }
}
