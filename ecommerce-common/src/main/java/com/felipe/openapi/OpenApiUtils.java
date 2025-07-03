package com.felipe.openapi;

import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

public class OpenApiUtils {
  private final Map<String, Schema> schemas = new HashMap<>();
  private final Map<String, ApiResponse> responses = new HashMap<>();
  private final Map<String, Example> examples = new HashMap<>();

  public static final String SCHEMAS_REF = "#/components/schemas/";

  public OpenApiUtils() {
  }

  public Map<String, Schema> getSchemas() {
    return this.schemas;
  }

  public Map<String, ApiResponse> getResponses() {
    return this.responses;
  }

  public Map<String, Example> getExamples() {
    return this.examples;
  }

  public void createSchemaFromClass(String schemaName, ModelConverters modelConverterInstance, Class<? > clazz, SchemaCustomizer customizer) {
    Schema<?> schema = modelConverterInstance.resolveAsResolvedSchema(new AnnotatedType(clazz)).schema;
    schema.setName(schemaName);
    schema.setType("object");
    customizer.customize(schema);
    this.schemas.put(schema.getName(), schema);
  }

  public void createSchema(String schemaName, SchemaCustomizer customizer) {
    Schema<?> schema = new Schema<>();
    schema.setName(schemaName);
    schema.setType("object");
    customizer.customize(schema);
    this.schemas.put(schema.getName(), schema);
  }

  public void createApiResponse(String responseName, String responseDescription, String mediaType, SchemaCustomizer customizer) {
    Schema<?> schema = new Schema<>();
    customizer.customize(schema);

    ApiResponse apiResponse = new ApiResponse();
    apiResponse.setDescription(responseDescription);
    apiResponse.setContent(new Content().addMediaType(mediaType, new MediaType().schema(schema)));
    this.responses.put(responseName, apiResponse);
  }

  public void createExample(String exampleName, ResponseType type, HttpStatus code, String message, @Nullable Object payload) {
    ResponsePayload<Object> example = new ResponsePayload.Builder<>()
      .type(type)
      .code(code)
      .message(message)
      .payload(payload)
      .build();
    this.examples.put(exampleName, new Example().value(example));
  }

  public void createExample(String exampleName, Object exampleObject) {
    Example example = new Example();
    example.setValue(exampleObject);
    this.examples.put(exampleName, example);
  }
}
