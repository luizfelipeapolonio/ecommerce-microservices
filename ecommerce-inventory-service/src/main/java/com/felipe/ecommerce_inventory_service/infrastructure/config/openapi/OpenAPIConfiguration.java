package com.felipe.ecommerce_inventory_service.infrastructure.config.openapi;

import com.felipe.ecommerce_inventory_service.core.application.dtos.category.CategoriesDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ImageFileDTO;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.core.domain.Model;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.BrandDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.CreateOrUpdateBrandDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CategoryDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.model.ModelDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.model.UpdateModelDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.product.CreateProductDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.product.ProductDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.product.ProductPageResponseDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.product.UpdateProductResponseDTO;
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
      .servers(List.of(new Server().url("http://localhost:8082")))
      .info(new Info()
        .title("Inventory Service API")
        .description("This lists all the Inventory Service API calls.")
        .version("1.0.0"))
      .tags(List.of(
        new Tag().name("Category").description("All category operations"),
        new Tag().name("Brand").description("All brand operations"),
        new Tag().name("Model").description("All model operations"),
        new Tag().name("Product").description("All product operations")
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
      this.apiUtils.createSchemaFromClass(
        "ModelDTO",
        modelConverterInstance,
        ModelDTO.class,
        SchemaCustomizer.withDefaults()
      );
      this.apiUtils.createSchemaFromClass(
        "UpdateModelDTO",
        modelConverterInstance,
        UpdateModelDTO.class,
        SchemaCustomizer.withDefaults()
      );
      this.apiUtils.createSchemaFromClass(
        "ProductDTO",
        modelConverterInstance,
        ProductDTO.class,
        SchemaCustomizer.withDefaults()
      );
      this.apiUtils.createSchemaFromClass(
        "ImageFileDTO",
        modelConverterInstance,
        ImageFileDTO.class,
        SchemaCustomizer.withDefaults()
      );
      this.apiUtils.createSchemaFromClass(
        "UpdateProductResponseDTO",
        modelConverterInstance,
        UpdateProductResponseDTO.class,
        SchemaCustomizer.withDefaults()
      );
      this.apiUtils.createSchemaFromClass(
        "ProductPageResponseDTO",
        modelConverterInstance,
        ProductPageResponseDTO.class,
        SchemaCustomizer.withDefaults()
      );
      this.apiUtils.createSchemaFromClass(
        "CreateProductDTO",
        modelConverterInstance,
        CreateProductDTO.class,
        schema -> {
          schema.addProperty("images", new ArraySchema()
            .items(new ObjectSchema().type("string").format("binary")));
          schema.addRequiredItem("images");
        }
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
      this.apiUtils.createSchema("ResponsePayload<List<BrandDTO>>", schema -> {
        schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
        schema.addAllOfItem(new ObjectSchema().addProperty("payload", new ArraySchema()
          .items(new ObjectSchema().$ref(SCHEMAS_REF + "BrandDTO"))));
      });
      this.apiUtils.createSchema("ResponsePayload<ModelDTO>", schema -> {
        schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
        schema.addAllOfItem(new ObjectSchema()
          .addProperty("payload", new ObjectSchema().$ref(SCHEMAS_REF + "ModelDTO")));
      });
      this.apiUtils.createSchema("ResponsePayload<List<ModelDTO>>", schema -> {
        schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
        schema.addAllOfItem(new ObjectSchema().addProperty("payload", new ArraySchema()
          .items(new ObjectSchema().$ref(SCHEMAS_REF + "ModelDTO"))));
      });
      this.apiUtils.createSchema("ResponsePayload<ProductDTO>", schema -> {
        schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
        schema.addAllOfItem(new ObjectSchema()
          .addProperty("payload", new ObjectSchema().$ref(SCHEMAS_REF + "ProductDTO")));
      });
      this.apiUtils.createSchema("ResponsePayload<UpdateProductResponseDTO>", schema -> {
        schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
        schema.addAllOfItem(new ObjectSchema()
          .addProperty("payload", new ObjectSchema().$ref(SCHEMAS_REF + "UpdateProductResponseDTO")));
      });
      this.apiUtils.createSchema("ResponsePayload<ProductPageResponseDTO>", schema -> {
        schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
        schema.addAllOfItem(new ObjectSchema()
          .addProperty("payload", new ObjectSchema().$ref(SCHEMAS_REF + "ProductPageResponseDTO")));
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
        .parentCategory(category2)
        .build();

      CustomValidationErrors validationErrors = new CustomValidationErrors();
      validationErrors.setField("id");
      validationErrors.setRejectedValue(-1);
      validationErrors.setCause("id must not be zero or a negative number");

      CustomValidationErrors validationErrors2 = new CustomValidationErrors();
      validationErrors2.setField("somevalue");
      validationErrors2.setRejectedValue("abcd");
      validationErrors2.setCause("Somevalue must be at least 6 characters long");

      BrandDTO brand1 = new BrandDTO(
        1L,
        "logitech",
        "A great brand",
        "2025-07-18T21:12:28.978228256",
        "2025-07-18T21:12:28.978228256"
      );
      BrandDTO brand2 = new BrandDTO(
        2L,
        "nvidia",
        "A great brand",
        "2025-07-18T21:12:28.978228256",
        "2025-07-18T21:12:28.978228256"
      );
      BrandDTO brand3 = new BrandDTO(
        3L,
        "corsair",
        "A great brand",
        "2025-07-18T21:12:28.978228256",
        "2025-07-18T21:12:28.978228256"
      );
      ModelDTO model1 = new ModelDTO(
        1L,
        "g pro",
        "A great model",
        "2025-07-18T21:12:28.978228256",
        "2025-07-18T21:12:28.978228256",
        brand1
      );
      ModelDTO model2 = new ModelDTO(
        2L,
        "g502 x",
        "A great model",
        "2025-07-18T21:12:28.978228256",
        "2025-07-18T21:12:28.978228256",
        brand1
      );

      Brand brand = Brand.builder()
        .id(1L)
        .name("logitech")
        .description("A great brand")
        .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
        .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
        .build();
      Model model = Model.builder()
        .id(1L)
        .name("g pro")
        .description("A great model")
        .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
        .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
        .brand(brand)
        .build();
      Product product = Product.builder()
        .id(UUID.fromString("b8dc2e49-20cd-42d2-9241-1d0535241688"))
        .name("Mouse wireless Logitech G PRO")
        .description("A technical and descriptive text about the product")
        .quantity(50)
        .unitPrice(new BigDecimal("120.00"))
        .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
        .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
        .category(subcategory3)
        .brand(brand)
        .model(model)
        .build();
      ImageFileDTO image1 = new ImageFileDTO(
        "b8dc2e49-20cd-42d2-9241-1d0535241688",
        "image1",
        "imagePath",
        "image/png",
        "123456",
        "image1",
        "thumbnail",
        "b8dc2e49-20cd-42d2-9241-1d0535241688",
        "2025-07-18T21:12:28.978228256",
        "2025-07-18T21:12:28.978228256"
      );
      ProductDTO productDTO = new ProductDTO(product, List.of(image1));
      UpdateProductResponseDTO updateProductResponseDTO = new UpdateProductResponseDTO(product);
      var productPageResponseDTO = new ProductPageResponseDTO(0, 1, 1, 1, List.of(productDTO));

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
        "AllSubcategoriesExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "All subcategories",
        List.of(subcategory3)
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
        "Brand '" + brand1.name() + "' created successfully",
        brand1
      );
      this.apiUtils.createExample(
        "ExistingBrandExample",
        ResponseType.ERROR,
        HttpStatus.CONFLICT,
        "Brand 'logitech' already exists",
        null
      );
      this.apiUtils.createExample(
        "GetAllBrandsExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "All brands",
        List.of(brand1, brand2, brand3)
      );
      this.apiUtils.createExample(
        "GetBrandByIdExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Found brand with id '" + brand1.id() + "'",
        brand1
      );
      this.apiUtils.createExample(
        "UpdateBrandExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Brand updated successfully",
        brand1
      );
      this.apiUtils.createExample(
        "DeleteBrandExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Brand 'hardware' deleted successfully",
        null
      );
      this.apiUtils.createExample(
        "CreateModelExample",
        ResponseType.SUCCESS,
        HttpStatus.CREATED,
        "Model 'g pro' created successfully",
        model1
      );
      this.apiUtils.createExample(
        "ExistingModelExample",
        ResponseType.ERROR,
        HttpStatus.CONFLICT,
        "Model 'g pro' already exists",
        null
      );
      this.apiUtils.createExample(
        "AllModelsOfBrandExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "All models of brand with id '1'",
        List.of(model1, model2)
      );
      this.apiUtils.createExample(
        "GetModelByIdExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Found model with id '" + model1.id() + "'",
        model1
      );
      this.apiUtils.createExample(
        "UpdateModelExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Model updated successfully",
        model1
      );
      this.apiUtils.createExample(
        "DeleteModelExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Model 'g pro' deleted successfully",
        null
      );
      this.apiUtils.createExample(
        "CreateProductExample",
        ResponseType.SUCCESS,
        HttpStatus.CREATED,
        "Product 'Mouse wireless Logitech G PRO' created successfully",
        productDTO
      );
      this.apiUtils.createExample(
        "ExistingProductExample",
        ResponseType.ERROR,
        HttpStatus.CONFLICT,
        "Product 'Mouse wireless Logitech G PRO' already exists",
        null
      );
      this.apiUtils.createExample(
        "CreateProductViolationExample",
        ResponseType.ERROR,
        HttpStatus.BAD_REQUEST,
        "Validation errors",
        List.of(validationErrors2)
      );
      this.apiUtils.createExample(
        "UpdateProductExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Product updated successfully",
        updateProductResponseDTO
      );
      this.apiUtils.createExample(
        "GetProductsByCategoryExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Products of 'mouse' category",
        productPageResponseDTO
      );
      this.apiUtils.createExample(
        "GetProductsByBrandExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Products of 'logitech' brand",
        productPageResponseDTO
      );
      this.apiUtils.createExample(
        "GetProductsByModelExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Products of 'g pro' model and 'logitech' brand",
        productPageResponseDTO
      );
      this.apiUtils.createExample(
        "GetProductsExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Products of category: 'mouse' - brand: 'logitech' - model: 'g pro'",
        productPageResponseDTO
      );
    };
  }
}