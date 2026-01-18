package com.felipe.ecommerce_cart_service.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
  private static final String[] DOCS_WHITELIST = {"/cart-service/swagger-ui.html", "/cart-service/swagger-ui/**",
                                                  "/cart-service/v3/api-docs/**"};

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(authorize -> authorize
        .requestMatchers(HttpMethod.GET, DOCS_WHITELIST).permitAll()
        .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/v1/carts").hasAuthority("SCOPE_admin")
        .anyRequest().authenticated())
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .oauth2ResourceServer(oauth2 -> oauth2
        .jwt(jwt -> jwt.jwtAuthenticationConverter(this.jwtAuthenticationConverter())))
      .build();
  }

  private JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
    return jwtAuthenticationConverter;
  }

  private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
    // Use 'scope' or 'scp' claim (the default) to extract authorities
    JwtGrantedAuthoritiesConverter defaultAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    // Use 'authorities' claim to extract authorities
    JwtGrantedAuthoritiesConverter customAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    customAuthoritiesConverter.setAuthorityPrefix("");
    customAuthoritiesConverter.setAuthoritiesClaimName("authorities");

    return jwt -> {
      List<GrantedAuthority> authorities = new ArrayList<>();
      authorities.addAll(defaultAuthoritiesConverter.convert(jwt));
      authorities.addAll(customAuthoritiesConverter.convert(jwt));
      return authorities;
    };
  }
}
