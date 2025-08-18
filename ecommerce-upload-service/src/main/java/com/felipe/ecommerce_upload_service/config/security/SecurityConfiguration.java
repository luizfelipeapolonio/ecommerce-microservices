package com.felipe.ecommerce_upload_service.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
  private static final String[] DOCS_WHITELIST = {"/upload-service/swagger-ui.html", "/upload-service/swagger-ui/**",
                                                  "/upload-service/v3/api-docs/**"};

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(authorize -> authorize
        .requestMatchers(HttpMethod.GET, DOCS_WHITELIST).permitAll()
        .anyRequest().hasAnyAuthority("ROLE_ADMIN", "SCOPE_admin"))
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
