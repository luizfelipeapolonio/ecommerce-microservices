package com.felipe.openapi;

import io.swagger.v3.oas.models.media.Schema;

/**
 * Callback interface that accepts a single input argument of type {@link Schema}
 * and returns no results.
 */
@FunctionalInterface
public interface SchemaCustomizer {
  /**
   * Performs the customizations on the provided schema.
   * @param schema the provided schema.
   */
  void customize(Schema schema);

  /**
   * Returns a {@link SchemaCustomizer} that does not alter the provided schema.
   * @return a {@link SchemaCustomizer} that does not alter the provided schema.
   */
  static SchemaCustomizer withDefaults() {
    return schema -> {
    };
  }
}
