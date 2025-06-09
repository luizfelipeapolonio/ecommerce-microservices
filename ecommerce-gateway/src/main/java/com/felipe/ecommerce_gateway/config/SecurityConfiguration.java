package com.felipe.ecommerce_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

  @Bean
  public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
    return http
      .csrf(ServerHttpSecurity.CsrfSpec::disable)
      .authorizeExchange(authorize -> authorize
        .pathMatchers("/api/v1/customers/signup").permitAll()
        .anyExchange().authenticated())
      .oauth2Login(Customizer.withDefaults())
      .securityMatcher(bearerAwareSecurityMatcher)
      .build();
  }

  // Custom security matcher - Rest api call support
  // if the request has a Bearer token, skip security entirely, allowing the request to
  // be forwarded
  private final ServerWebExchangeMatcher bearerAwareSecurityMatcher = exchange -> {
    String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      return MatchResult.notMatch(); // Skip security
    }
    return MatchResult.match(); // Apply security (OAuth2 Login)
  };
}
