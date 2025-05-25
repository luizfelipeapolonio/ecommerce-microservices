package com.felipe.ecommerce_auth_server.config.security;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.felipe.ecommerce_auth_server.persistence.entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
@JsonAutoDetect(
  fieldVisibility = JsonAutoDetect.Visibility.ANY,
  getterVisibility = JsonAutoDetect.Visibility.NONE,
  isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = CustomUserPrincipalDeserializer.class)
public class UserPrincipal implements UserDetails {
  private final UUID id;
  private final String email;
  //@JsonIgnore
  private final String password;
  private final String role;

  public UserPrincipal(UserEntity userEntity) {
    this.id = userEntity.getId();
    this.email = userEntity.getEmail();
    this.password = userEntity.getPassword();
    this.role = userEntity.getRole();
  }

  public UserPrincipal(UUID id, String email, String password, String role) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  public UUID getId() {
    return this.id;
  }

  public String email() {
    return this.email;
  }

  public String getRole() {
    return this.role;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.email;
  }
}
