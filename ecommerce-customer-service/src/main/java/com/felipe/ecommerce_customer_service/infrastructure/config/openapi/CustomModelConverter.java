package com.felipe.ecommerce_customer_service.infrastructure.config.openapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import org.springframework.stereotype.Component;
import com.felipe.openapi.CustomTypeNameResolver;

@Component
public class CustomModelConverter extends ModelResolver {
  public CustomModelConverter(ObjectMapper mapper) {
    super(mapper, new CustomTypeNameResolver());
  }
}
