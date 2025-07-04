package com.felipe.ecommerce_gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

  @Bean
  public OpenAPI gatewayOpenAPI() {
    return new OpenAPI()
      .info(new Info()
        .title("E-commerce Application Microservices APIs")
        .description("Documentation for all the E-commerce microservices"));
  }
}
