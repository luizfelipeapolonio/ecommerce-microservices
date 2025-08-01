package com.felipe.ecommerce_inventory_service.infrastructure.config.openapi;

import com.felipe.ecommerce_inventory_service.core.application.dtos.CategoriesDTO;
import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.BrandDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.CreateOrUpdateBrandDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CategoryDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CreateOrUpdateCategoryDTO;
import com.felipe.openapi.OpenApiUtils;
import com.felipe.openapi.SchemaCustomizer;
import com.felipe.response.CustomValidationErrors;
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

import java.time.LocalDateTime;
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
      .tags(List.of(
        new Tag().name("Category").description("All category operations"),
        new Tag().name("Brand").description("All brand operations")
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
      ModelConverters modelConverterInstance = ModelConverters.getInstance();
      this.apiUtils.createSchemaFromClass(
        "CategoryDTO",
        modelConverterInstance,
        CategoryDTO.class,
        schema -> schema.title("CategoryDTO")
          .addProperty("parentCategory", new ObjectSchema()
          .$ref(SCHEMAS_REF + "CategoryDTO"))
      );
      this.apiUtils.createSchemaFromClass(
        "CategoriesDTO",
        modelConverterInstance,
        CategoriesDTO.class,
        SchemaCustomizer.withDefaults()
      );
      this.apiUtils.createSchemaFromClass(
        "CreateOrUpdateBrandDTO",
        modelConverterInstance,
        CreateOrUpdateBrandDTO.class,
        SchemaCustomizer.withDefaults()
      );
      this.apiUtils.createSchemaFromClass(
        "BrandDTO",
        modelConverterInstance,
        BrandDTO.class,
        SchemaCustomizer.withDefaults()
      );
      this.apiUtils.createSchema("CategoryDomainDTO", schema -> {
        schema.addProperty("id", new ObjectSchema().type("integer").format("int64"));
        schema.addProperty("name", new ObjectSchema().type("string"));
        schema.addProperty("createdAt", new ObjectSchema().type("string"));
        schema.addProperty("updatedAt", new ObjectSchema().type("string"));
      });
      this.apiUtils.createSchema("ResponsePayload<CategoryDTO>", schema -> {
        schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
        schema.addAllOfItem(new ObjectSchema()
          .addProperty("payload", new ObjectSchema().$ref(SCHEMAS_REF + "CategoryDTO")));
      });
      this.apiUtils.createSchema("ResponsePayload<List<CategoryDTO>>", schema -> {
        schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
        schema.addAllOfItem(new ObjectSchema().addProperty("payload", new ArraySchema()
            .items(new ObjectSchema().$ref(SCHEMAS_REF + "CategoryDTO"))));
      });
      this.apiUtils.createSchema("ResponsePayload<List<CategoriesDTO>>", schema -> {
        schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
        schema.addAllOfItem(new ObjectSchema().addProperty("payload", new ArraySchema()
          .items(new ObjectSchema().$ref(SCHEMAS_REF + "CategoriesDTO"))));
      });
      this.apiUtils.createSchema("ResponsePayload<BrandDTO>", schema -> {
        schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
        schema.addAllOfItem(new ObjectSchema()
          .addProperty("payload", new ObjectSchema().$ref(SCHEMAS_REF + "BrandDTO")));
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
      Category category1 = Category.builder()
        .id(1L)
        .name("hardware")
        .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
        .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
        .build();
      Category subcategory1 = Category.builder()
        .id(2L)
        .name("motherboards")
        .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
        .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
        .build();
      Category subcategory2 = Category.builder()
        .id(3L)
        .name("cpus")
        .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
        .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
        .build();
      Category category2 = Category.builder()
        .id(4L)
        .name("peripherals")
        .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
        .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
        .build();
      Category subcategory3 = Category.builder()
        .id(5L)
        .name("mouse")
        .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
        .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
        .build();

      CustomValidationErrors validationErrors = new CustomValidationErrors();
      validationErrors.setField("id");
      validationErrors.setRejectedValue(-1);
      validationErrors.setCause("id must not be zero or a negative number");

      BrandDTO brandDTO = new BrandDTO(
        1L,
        "logitech",
        "A great brand",
        "2025-07-18T21:12:28.978228256",
        "2025-07-18T21:12:28.978228256"
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
        "Subcategory '" + categoryWithParentCategory.name() + "' created successfully",
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
      this.apiUtils.createExample(
        "GetCategoryByIdExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Found category with id '" + categoryWithNoParentCategory.id() + "'",
        categoryWithNoParentCategory
      );
      this.apiUtils.createExample(
        "ConstraintViolationExample",
        ResponseType.ERROR,
        HttpStatus.BAD_REQUEST,
        "Validation errors",
        new CustomValidationErrors[] {validationErrors}
      );
      this.apiUtils.createExample(
        "ListOfCategoriesExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "All categories",
        List.of(
          new CategoriesDTO(category1, List.of(subcategory1, subcategory2)),
          new CategoriesDTO(category2, List.of(subcategory3))
        )
      );
      this.apiUtils.createExample(
        "DeleteCategoryExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Category 'hardware' deleted successfully",
        null
      );
      this.apiUtils.createExample(
        "CreateBrandExample",
        ResponseType.SUCCESS,
        HttpStatus.CREATED,
        "Brand '" + brandDTO.name() + "' created successfully",
        brandDTO
      );
      this.apiUtils.createExample(
        "ExistingBrandExample",
        ResponseType.ERROR,
        HttpStatus.CONFLICT,
        "Brand 'logitech' already exists",
        null
      );
    };
  }
}
