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

  @Value("${auth.clients.customer-client.secret}")
  private String customerClientSecret;

  @Value("${auth.clients.inventory-client.secret}")
  private String inventoryClientSecret;

  @Value("${auth.clients.discount-client.secret}")
  private String discountClientSecret;

  @Value("${auth.clients.cart-client.secret}")
  private String cartClientSecret;

  @Value("${auth.clients.order-client.secret}")
  private String orderClientSecret;

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

      RegisteredClient customerClient = RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId("ecommerce-customer-service")
        .clientSecret(passwordEncoder.encode(customerClientSecret))
        .clientName("ecommerce-customer-service")
        .clientIdIssuedAt(Instant.now())
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
        .scope("read")
        .scope("write")
        .scope("admin")
        .build();

      RegisteredClient inventoryClient = RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId("ecommerce-inventory-service")
        .clientSecret(passwordEncoder.encode(inventoryClientSecret))
        .clientName("ecommerce-inventory-service")
        .clientIdIssuedAt(Instant.now())
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
        .scope("admin")
        .build();

      RegisteredClient discountClient = RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId("ecommerce-discount-service")
        .clientSecret(passwordEncoder.encode(discountClientSecret))
        .clientName("ecommerce-discount-service")
        .clientIdIssuedAt(Instant.now())
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
        .scope("admin")
        .build();

      RegisteredClient cartClient = RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId("ecommerce-cart-service")
        .clientSecret(passwordEncoder.encode(cartClientSecret))
        .clientName("ecommerce-cart-service")
        .clientIdIssuedAt(Instant.now())
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
        .scope("admin")
        .build();

      RegisteredClient orderClient = RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId("ecommerce-order-service")
        .clientSecret(passwordEncoder.encode(orderClientSecret))
        .clientName("ecommerce-order-service")
        .clientIdIssuedAt(Instant.now())
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
        .scope("admin")
        .build();

      jpaRegisteredClientRepository.save(gatewayClient);
      jpaRegisteredClientRepository.save(customerClient);
      jpaRegisteredClientRepository.save(inventoryClient);
      jpaRegisteredClientRepository.save(discountClient);
      jpaRegisteredClientRepository.save(cartClient);
      jpaRegisteredClientRepository.save(orderClient);
    };
  }
}
