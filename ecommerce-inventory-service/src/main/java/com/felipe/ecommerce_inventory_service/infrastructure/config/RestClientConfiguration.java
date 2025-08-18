package com.felipe.ecommerce_inventory_service.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

import java.util.List;

@Configuration
public class RestClientConfiguration {

  @Bean
  public RestClient restClient(OAuth2AuthorizedClientManager authorizedClientManager) {
    OAuth2ClientHttpRequestInterceptor requestInterceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setSupportedMediaTypes(List.of(MediaType.MULTIPART_FORM_DATA));

    return RestClient.builder()
      .requestInterceptor(requestInterceptor)
      .messageConverters(converters -> converters.add(converter))
      .build();
  }
}
