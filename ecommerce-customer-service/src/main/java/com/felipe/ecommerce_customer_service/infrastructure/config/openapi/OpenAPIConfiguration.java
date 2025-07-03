package com.felipe.ecommerce_customer_service.infrastructure.config.openapi;

import com.felipe.ecommerce_customer_service.core.application.dtos.UpdateCustomerDTO;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.AddressDTO;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CustomerDTO;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CustomerProfileDTO;
import com.felipe.openapi.OpenApiUtils;
import com.felipe.openapi.SchemaCustomizer;
import com.felipe.response.CustomValidationErrors;
import com.felipe.response.ResponsePayload;
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

import java.util.List;

import static com.felipe.openapi.OpenApiUtils.SCHEMAS_REF;

@OpenAPIDefinition
@Configuration
public class OpenAPIConfiguration {
  private final OpenApiUtils apiUtils = new OpenApiUtils();
  public static final String BEARER_TOKEN_AUTH = "bearer_token_auth";

  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
      .servers(List.of(new Server().url("http://localhost:8080")))
      .info(new Info()
        .title("Customer Service API")
        .description("This lists all the Customer Service API calls.")
        .version("1.0.0"))
      .tags(List.of(new Tag()
        .name("Customers")
        .description("All customer operations")))
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
        "ResponsePayload<Void>",
        modelConverterInstance,
        ResponsePayload.class,
        SchemaCustomizer.withDefaults()
      );
      this.apiUtils.createSchemaFromClass(
        "CustomValidationErrors",
        modelConverterInstance,
        CustomValidationErrors.class,
        SchemaCustomizer.withDefaults()
      );
      this.apiUtils.createSchemaFromClass(
        "CustomerDTO",
        modelConverterInstance,
        CustomerDTO.class,
        SchemaCustomizer.withDefaults()
      );
      this.apiUtils.createSchemaFromClass(
        "AddressDTO",
        modelConverterInstance,
        AddressDTO.class,
        SchemaCustomizer.withDefaults()
      );
      this.apiUtils.createSchemaFromClass(
        "CustomerProfileDTO",
        modelConverterInstance,
        CustomerProfileDTO.class,
        SchemaCustomizer.withDefaults()
      );
      this.apiUtils.createSchemaFromClass(
        "UpdateCustomerDTO",
        modelConverterInstance,
        UpdateCustomerDTO.class,
        schema -> {
          schema.addProperty("username", new ObjectSchema()
            .type("string")
            .example("UpdatedUsername")
            .nullable(true));
          schema.addProperty("firstName", new ObjectSchema()
            .type("string")
            .example("Updated FirstName")
            .nullable(true));
          schema.addProperty("lastName", new ObjectSchema()
            .type("string")
            .example("Updated LastName")
            .nullable(true));
      });
      this.apiUtils.createSchema(
        "UpdateAddressDTO",
        schema -> {
          schema.addProperty("street", new ObjectSchema()
            .type("string")
            .nullable(true));
          schema.addProperty("number", new ObjectSchema()
            .type("string")
            .nullable(true));
          schema.addProperty("complement", new ObjectSchema()
            .type("string")
            .nullable(true));
          schema.addProperty("district", new ObjectSchema()
            .type("string")
            .nullable(true));
          schema.addProperty("zipcode", new ObjectSchema()
            .type("string")
            .nullable(true));
          schema.addProperty("city", new ObjectSchema()
            .type("string")
            .nullable(true));
          schema.addProperty("state", new ObjectSchema()
            .type("string")
            .nullable(true));
          schema.addProperty("country", new ObjectSchema()
            .type("string")
            .nullable(true));
      });
      this.apiUtils.createSchema("ResponsePayload<CustomerProfileDTO>", schema -> {
        schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
        schema.addAllOfItem(new ObjectSchema()
          .addProperty("payload", new ObjectSchema().$ref(SCHEMAS_REF + "CustomerProfileDTO")));
      });
      this.apiUtils.createSchema("ResponsePayload<CustomerDTO>", schema -> {
        schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
        schema.addAllOfItem(new ObjectSchema()
          .addProperty("type", new ObjectSchema().type("string").example("success"))
          .addProperty("code", new ObjectSchema().type("integer").format("int32").example(201))
          .addProperty("message", new ObjectSchema().type("string").example("Customer account created successfully"))
          .addProperty("payload", new ObjectSchema().$ref(SCHEMAS_REF + "CustomerDTO")));
      });
      this.apiUtils.createSchema("ResponsePayload<List<CustomValidationErrors>>", schema -> {
        schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + "ResponsePayload<Void>"));
        schema.addAllOfItem(new ObjectSchema()
          .addProperty("payload", new ArraySchema()
            .items(new ObjectSchema().$ref(SCHEMAS_REF + "CustomValidationErrors"))));
      });

      // Global API response schemas
      this.apiUtils.createApiResponse(
        "ValidationErrors",
        "Returns an error response with the fields validation errors",
        "application/json",
        schema -> {
          final String schemaName = "ResponsePayload<List<CustomValidationErrors>>";
          schema.setTitle(schemaName);
          schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + schemaName));
          schema.addAllOfItem(new ObjectSchema()
            .addProperty("type", new ObjectSchema().type("string").example("error")))
            .addProperty("code", new ObjectSchema().type("integer").format("int32").example(422))
            .addProperty("message", new ObjectSchema().type("string").example("Validation errors"));
        }
      );

      this.apiUtils.createApiResponse(
        "NotFound",
        "Resource not found",
        "application/json",
        schema -> {
          final String schemaName = "ResponsePayload<Void>";
          schema.setTitle(schemaName);
          schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + schemaName));
          schema.addAllOfItem(new ObjectSchema()
            .addProperty("type", new ObjectSchema().type("string").example("error"))
            .addProperty("code", new ObjectSchema().type("integer").format("int32").example(404))
            .addProperty("message", new ObjectSchema().type("string").example("Customer with email 'john@email.com' not found")))
            .addProperty("payload", new ObjectSchema().example(null));
        });
      this.apiUtils.createApiResponse(
        "InternalServerError",
        "Internal Server Error",
        "application/json",
        schema -> {
          final String schemaName = "ResponsePayload<Void>";
          schema.setTitle(schemaName);
          schema.addAllOfItem(new ObjectSchema().$ref(SCHEMAS_REF + schemaName));
          schema.addAllOfItem(new ObjectSchema()
            .addProperty("type", new ObjectSchema().type("string").example("error"))
            .addProperty("code", new ObjectSchema().type("integer").format("int32").example(500))
            .addProperty("message", new ObjectSchema().type("string").example("An internal server error occurred")))
            .addProperty("payload", new ObjectSchema().example(null));
        });

      // Examples
      this.apiUtils.createExample(
        "EmailConflict",
        ResponseType.ERROR,
        HttpStatus.CONFLICT,
        "E-mail 'john@email.com' already exists",
        null
      );
      AddressDTO addressDTO = new AddressDTO(
        "Some street name",
        "124B",
        "apartment",
        "DistrictName",
        "12345-678",
        "CityName",
        "StateName",
        "CountryName"
      );

      CustomerProfileDTO customerProfileDTO = new CustomerProfileDTO(
        "f6043015-5e69-435f-98eb-94ad328d3a4a",
        "john@email.com",
        "johndoe",
        "John",
        "Doe",
        "2025-06-09 18:32:26.711469",
        "2025-06-09 18:32:26.711469",
        addressDTO
      );

      this.apiUtils.createExample(
        "GetCustomerProfileExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Authenticated customer profile",
        customerProfileDTO
      );
      this.apiUtils.createExample(
        "UpdateCustomerExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Customer profile updated successfully",
        customerProfileDTO
      );
      this.apiUtils.createExample("InsertAddressDTOExample", addressDTO);
      this.apiUtils.createExample(
        "InsertAddressExample",
        ResponseType.SUCCESS,
        HttpStatus.CREATED,
        "Address of customer with email 'john@email.com' inserted successfully",
        customerProfileDTO
      );
      this.apiUtils.createExample(
        "UpdateAddressExample",
        ResponseType.SUCCESS,
        HttpStatus.OK,
        "Address of customer with email 'john@email.com' updated successfully",
        customerProfileDTO
      );
    };
  }
}
