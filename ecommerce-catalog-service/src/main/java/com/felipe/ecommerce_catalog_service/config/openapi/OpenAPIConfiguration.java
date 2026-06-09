package com.felipe.ecommerce_catalog_service.config.openapi;

import com.felipe.ecommerce_catalog_service.dtos.HomepageProductsDTO;
import com.felipe.openapi.OpenApiUtils;
import com.felipe.openapi.SchemaCustomizer;
import com.felipe.response.ResponseType;
import com.felipe.response.product.BrandDTO;
import com.felipe.response.product.CategoryDTO;
import com.felipe.response.product.ImageFileDTO;
import com.felipe.response.product.ModelDTO;
import com.felipe.response.product.ProductResponseDTO;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.felipe.openapi.OpenApiUtils.SCHEMAS_REF;

@OpenAPIDefinition
@Configuration
public class OpenAPIConfiguration {
  private final OpenApiUtils openApiUtils = OpenApiUtils.getInstanceWithCommonSchemas();
  public static final String BEARER_TOKEN_AUTH = "bearer_token_auth";

  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
      .servers(List.of(new Server().url("http://localhost:8090")))
      .info(new Info()
        .title("Catalog Service API")
        .description("This lists all the Catalog Service API calls")
        .version("1.0.0"))
      .tags(List.of(new Tag().name("Catalog").description("All catalog operations")))
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
      "ImageFileDTO",
      modelConvertersInstance,
      ImageFileDTO.class,
      SchemaCustomizer.withDefaults()
    );
    this.openApiUtils.createSchemaFromClass(
      "CategoryDTO",
      modelConvertersInstance,
      CategoryDTO.class,
      SchemaCustomizer.withDefaults()
    );
    this.openApiUtils.createSchemaFromClass(
      "BrandDTO",
      modelConvertersInstance,
      BrandDTO.class,
      SchemaCustomizer.withDefaults()
    );
    this.openApiUtils.createSchemaFromClass(
      "ModelDTO",
      modelConvertersInstance,
      ModelDTO.class,
      SchemaCustomizer.withDefaults()
    );
    this.openApiUtils.createSchemaFromClass(
      "ProductResponseDTO",
      modelConvertersInstance,
      ProductResponseDTO.class,
      SchemaCustomizer.withDefaults()
    );
    this.openApiUtils.createSchemaFromClass(
      "HomepageProductsDTO",
      modelConvertersInstance,
      HomepageProductsDTO.class,
      SchemaCustomizer.withDefaults()
    );
    this.openApiUtils.createSchema("ResponsePayload<HomepageProductsDTO>", schema -> {
      schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
      schema.addAllOfItem(new ObjectSchema()
        .addProperty("payload", new ObjectSchema().$ref(SCHEMAS_REF + "HomepageProductsDTO")));
    });
  }

  private void insertExamples() {
    BrandDTO brandDTO = new BrandDTO(1L, "brand", "A brand description", "2026-06-02T16:34:53.638927", "2026-06-02T16:34:53.638927");
    ProductResponseDTO product = new ProductResponseDTO(
      "b8dc2e49-20cd-42d2-9241-1d0535241688",
      "Product 1",
      "Description of product 1",
      "150.00",
      20L,
      false,
      null,
      null,
      "2026-06-02T16:34:53.638927",
      "2026-06-02T16:34:53.638927",
      new CategoryDTO(1L, "category", "2026-06-02T16:34:53.638927", "2026-06-02T16:34:53.638927", null),
      brandDTO,
      new ModelDTO(1L, "model", "A model description", "2026-06-02T16:34:53.638927", "2026-06-02T16:34:53.638927", brandDTO),
      List.of(new ImageFileDTO(
        "6f5fc7f2-9f39-448a-aae6-73bd396b455a",
        "Image-of-product",
        "/path/to/image.jpg",
        "jpg",
        "23.00MB",
        "original-image-filename",
        "thumbnail",
        "b8dc2e49-20cd-42d2-9241-1d0535241688",
        "2026-06-02T16:34:53.638927",
        "2026-06-02T16:34:53.638927"
      ))
    );
    HomepageProductsDTO homepageProductsDTO = new HomepageProductsDTO(
      1,
      List.of(product)
    );

    this.openApiUtils.createExample(
      "GetHomepageProductsExample",
      ResponseType.SUCCESS,
      HttpStatus.OK,
      "Catalog",
      homepageProductsDTO
    );
  }
}
