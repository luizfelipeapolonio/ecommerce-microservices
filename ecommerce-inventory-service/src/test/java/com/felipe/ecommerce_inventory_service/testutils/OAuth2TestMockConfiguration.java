package com.felipe.ecommerce_inventory_service.testutils;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

@TestConfiguration
public class OAuth2TestMockConfiguration {

  @Bean
  public ClientRegistrationRepository clientRegistrationRepository() {
    ClientRegistration testClient = ClientRegistration.withRegistrationId("oauth2-test-client")
      .clientId("test-client")
      .clientName("oauth2-test-client")
      .clientSecret("test-secret")
      .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
      .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
      .issuerUri("http://localhost/fake-uri")
      .tokenUri("http://localhost/fake-token")
      .userInfoUri("http://localhost/fake-userinfo")
      .scope("test")
      .build();

    return new InMemoryClientRegistrationRepository(testClient);
  }

  @Bean
  public OAuth2AuthorizedClientService authorizedClient(ClientRegistrationRepository clientRegistrationRepository) {
    return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
  }
}
