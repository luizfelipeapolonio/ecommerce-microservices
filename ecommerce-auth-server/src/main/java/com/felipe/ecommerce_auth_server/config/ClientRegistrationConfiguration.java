package com.felipe.ecommerce_auth_server.config;

import com.felipe.ecommerce_auth_server.persistence.repositories.JpaRegisteredClientRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Configuration
public class ClientRegistrationConfiguration {
  private final JpaRegisteredClientRepository jpaRegisteredClientRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${auth.clients.gateway-client.secret}")
  private String gatewayClientSecret;

  public ClientRegistrationConfiguration(JpaRegisteredClientRepository jpaRegisteredClientRepository, PasswordEncoder passwordEncoder) {
    this.jpaRegisteredClientRepository = jpaRegisteredClientRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Bean
  public CommandLineRunner registerClients() {
    return args -> {
      RegisteredClient gatewayClient = RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId("ecommerce-gateway")
        .clientSecret(passwordEncoder.encode(gatewayClientSecret))
        .clientName("ecommerce-gateway")
        .clientIdIssuedAt(Instant.now())
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
        .redirectUri("http://127.0.0.1:8080/login/oauth2/code/ecommerce-gateway")
        .scope(OidcScopes.OPENID)
        .scope(OidcScopes.PROFILE)
        .tokenSettings(TokenSettings.builder()
          .accessTokenTimeToLive(Duration.ofMinutes(1))
          .build())
        .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
        .build();

      jpaRegisteredClientRepository.save(gatewayClient);
    };
  }
}
