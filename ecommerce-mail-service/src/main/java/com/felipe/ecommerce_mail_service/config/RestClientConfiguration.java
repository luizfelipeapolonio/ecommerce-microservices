package com.felipe.ecommerce_mail_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

  /*
  * The error java.lang.IllegalArgumentException: servletRequest cannot be null occurs when using a RestClient
  * (or WebClient) with OAuth2 inside a Kafka listener because Kafka consumers operate outside the context
  * of an HTTP request.
  *
  * By default, many Spring Security OAuth2 configurations use the DefaultOAuth2AuthorizedClientManager,
  * which expects an active HttpServletRequest to manage tokens.
  * Since a Kafka listener runs in its own background thread pool, no such request context exists.
  *
  * To resolve this, we must switch from the web-scoped manager to one designed for background services.
  *
  * Change the AuthorizedClientManager:
  *  - Replace the DefaultOAuth2AuthorizedClientManager with the AuthorizedClientServiceOAuth2AuthorizedClientManager.
  *    This implementation uses a service (like a database or in-memory map) to manage tokens instead of the current session or request.
  */
  @Bean
  public OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
                                                               OAuth2AuthorizedClientService authorizedClientService) {
    return new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
  }

  @Bean
  public RestClient restClient(OAuth2AuthorizedClientManager authorizedClientManager) {
    OAuth2ClientHttpRequestInterceptor requestInterceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);
    return RestClient.builder()
      .requestInterceptor(requestInterceptor)
      .build();
  }
}
