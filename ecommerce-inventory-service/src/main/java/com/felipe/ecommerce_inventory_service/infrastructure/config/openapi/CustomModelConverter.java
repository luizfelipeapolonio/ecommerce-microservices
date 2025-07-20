package com.felipe.ecommerce_inventory_service.infrastructure.config.openapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipe.openapi.CustomTypeNameResolver;
import io.swagger.v3.core.jackson.ModelResolver;
import org.springframework.stereotype.Component;

@Component
public class CustomModelConverter extends ModelResolver {
  public CustomModelConverter(ObjectMapper mapper) {
    super(mapper, new CustomTypeNameResolver());
  }
}
