package com.felipe.ecommerce_auth_server.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "authorization_consent")
@IdClass(AuthorizationConsentEntity.AuthorizationConsentId.class)
public class AuthorizationConsentEntity {

  @Id
  @Column(name = "registered_client_id")
  private String registeredClientId;

  @Id
  @Column(name = "principal_name")
  private String principalName;

  @Column(length = 1000)
  private String authorities;

  public AuthorizationConsentEntity() {}

  public String getRegisteredClientId() {
    return this.registeredClientId;
  }

  public void setRegisteredClientId(String registeredClientId) {
    this.registeredClientId = registeredClientId;
  }

  public String getPrincipalName() {
    return this.principalName;
  }

  public void setPrincipalName(String principalName) {
    this.principalName = principalName;
  }

  public String getAuthorities() {
    return this.authorities;
  }

  public void setAuthorities(String authorities) {
    this.authorities = authorities;
  }

  public static class AuthorizationConsentId implements Serializable {
    private String registeredClientId;
    private String principalName;

    public String getRegisteredClientId() {
      return registeredClientId;
    }

    public void setRegisteredClientId(String registeredClientId) {
      this.registeredClientId = registeredClientId;
    }

    public String getPrincipalName() {
      return principalName;
    }

    public void setPrincipalName(String principalName) {
      this.principalName = principalName;
    }

    @Override
    public boolean equals(Object obj) {
      if(this == obj) return true;
      if(obj == null || getClass() != obj.getClass()) return false;
      AuthorizationConsentId that = (AuthorizationConsentId) obj;
      return registeredClientId.equals(that.registeredClientId) && principalName.equals(that.principalName);
    }

    @Override
    public int hashCode() {
      return Objects.hash(registeredClientId, principalName);
    }
  }
}
