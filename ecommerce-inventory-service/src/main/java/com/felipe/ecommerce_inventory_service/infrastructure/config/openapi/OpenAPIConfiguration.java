package com.felipe.ecommerce_inventory_service.infrastructure.config.openapi;

import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CategoryDTO;
import com.felipe.openapi.OpenApiUtils;
import com.felipe.response.ResponseType;
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
  private final OpenApiUtils apiUtils = OpenApiUtils.getInstanceWithCommonSchemas();
  public static final String BEARER_TOKEN_AUTH = "bearer_token_auth";

  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
      .servers(List.of(new Server().url("http://localhost:8082")))
      .info(new Info()
        .title("Inventory Service API")
        .description("This lists all the Inventory Service API calls.")
        .version("1.0.0"))
      .tags(List.of(new Tag()
        .name("Inventory")
        .description("All inventory operations")))
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
      ModelConverters modelConverterInstance = ModelConverters.getInstance();
      this.apiUtils.createSchemaFromClass(
        "CategoryDTO",
        modelConverterInstance,
        CategoryDTO.class,
        schema -> schema.title("CategoryDTO")
          .addProperty("parentCategory", new ObjectSchema()
          .$ref(SCHEMAS_REF + "CategoryDTO"))
      );
      this.apiUtils.createSchema("ResponsePayload<CategoryDTO>", schema -> {
        schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
        schema.addAllOfItem(new ObjectSchema()
          .addProperty("payload", new ObjectSchema().$ref(SCHEMAS_REF + "CategoryDTO")));
      });

      // Examples
      CategoryDTO categoryWithNoParentCategory = new CategoryDTO(
        1L,
        "hardware",
        "2025-07-18T21:12:28.978228256",
        "2025-07-18T21:12:28.978228256",
        null
        );
      CategoryDTO categoryWithParentCategory = new CategoryDTO(
        2L,
        "motherboards",
        "2025-07-18T21:12:28.978228256",
        "2025-07-18T21:12:28.978228256",
        categoryWithNoParentCategory
      );

      this.apiUtils.createExample(
        "CategoryDTOWithNoSubcategoryExample",
        ResponseType.SUCCESS,
        HttpStatus.CREATED,
        "Category '" + categoryWithNoParentCategory.name() + "' created successfully",
        categoryWithNoParentCategory
      );
      this.apiUtils.createExample(
        "CreateSubcategoryExample",
        ResponseType.SUCCESS,
        HttpStatus.CREATED,
        "Subcategoria '" + categoryWithParentCategory.name() + "' criada com sucesso",
        categoryWithParentCategory
      );
      this.apiUtils.createExample(
        "UpdateCategoryExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Category updated successfully",
        categoryWithNoParentCategory
      );
      this.apiUtils.createExample(
        "ExistingCategoryExample",
        ResponseType.ERROR,
        HttpStatus.CONFLICT,
        "Category 'hardware' already exists",
        null
      );
    };
  }
}
