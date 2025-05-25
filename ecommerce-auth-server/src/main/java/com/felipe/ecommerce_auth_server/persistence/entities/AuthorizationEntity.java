package com.felipe.ecommerce_auth_server.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "oauth_authorization")
public class AuthorizationEntity {

  @Id
  private String id;

  @Column(name = "registered_client_id")
  private String registeredClientId;

  @Column(name = "principal_name")
  private String principalName;

  @Column(name = "authorization_grant_type")
  private String authorizationGrantType;

  @Column(name = "authorized_scopes", length = 1000)
  private String authorizedScopes;

  @Column(length = 4000)
  private String attributes;

  @Column(length = 500)
  private String state;

  @Column(name = "authorization_code_value", length = 4000)
  private String authorizationCodeValue;

  @Column(name = "authorization_code_issued_at")
  private Instant authorizationCodeIssuedAt;

  @Column(name = "authorization_code_expires_at")
  private Instant authorizationCodeExpiresAt;

  @Column(name = "authorization_code_metadata")
  private String authorizationCodeMetadata;

  @Column(name = "access_token_value", length = 4000)
  private String accessTokenValue;

  @Column(name = "access_token_issued_at")
  private Instant accessTokenIssuedAt;

  @Column(name = "access_token_expires_at")
  private Instant accessTokenExpiresAt;

  @Column(name = "access_token_metadata", length = 2000)
  private String accessTokenMetadata;

  @Column(name = "access_token_type")
  private String accessTokenType;

  @Column(name = "access_token_scopes", length = 1000)
  private String accessTokenScopes;

  @Column(name = "refresh_token_value", length = 4000)
  private String refreshTokenValue;

  @Column(name = "refresh_token_issued_at")
  private Instant refreshTokenIssuedAt;

  @Column(name = "refresh_token_expires_at")
  private Instant refreshTokenExpiresAt;

  @Column(name = "refresh_token_metadata", length = 2000)
  private String refreshTokenMetadata;

  @Column(name = "oidc_id_token_value", length = 4000)
  private String oidcIdTokenValue;

  @Column(name = "oidc_id_token_issued_at")
  private Instant oidcIdTokenIssuedAt;

  @Column(name = "oidc_id_token_expires_at")
  private Instant oidcIdTokenExpiresAt;

  @Column(name = "oidc_id_token_metadata", length = 2000)
  private String oidcIdTokenMetadata;

  @Column(name = "oidc_id_token_claims", length = 2000)
  private String oidcIdTokenClaims;

  @Column(name = "user_code_value", length = 4000)
  private String userCodeValue;

  @Column(name = "user_code_issued_at")
  private Instant userCodeIssuedAt;

  @Column(name = "user_code_expires_at")
  private Instant userCodeExpiresAt;

  @Column(name = "user_code_metadata", length = 2000)
  private String userCodeMetadata;

  @Column(name = "device_code_value", length = 4000)
  private String deviceCodeValue;

  @Column(name = "device_code_issued_at")
  private Instant deviceCodeIssuedAt;

  @Column(name = "device_code_expires_at")
  private Instant deviceCodeExpiresAt;

  @Column(name = "device_code_metadata", length = 2000)
  private String deviceCodeMetadata;

  public AuthorizationEntity() {}

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

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

  public String getAuthorizationGrantType() {
    return this.authorizationGrantType;
  }

  public void setAuthorizationGrantType(String authorizationGrantType) {
    this.authorizationGrantType = authorizationGrantType;
  }

  public String getAuthorizedScopes() {
    return this.authorizedScopes;
  }

  public void setAuthorizedScopes(String authorizedScopes) {
    this.authorizedScopes = authorizedScopes;
  }

  public String getAttributes() {
    return this.attributes;
  }

  public void setAttributes(String attributes) {
    this.attributes = attributes;
  }

  public String getState() {
    return this.state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getAuthorizationCodeValue() {
    return this.authorizationCodeValue;
  }

  public void setAuthorizationCodeValue(String authorizationCodeValue) {
    this.authorizationCodeValue = authorizationCodeValue;
  }

  public Instant getAuthorizationCodeIssuedAt() {
    return this.authorizationCodeIssuedAt;
  }

  public void setAuthorizationCodeIssuedAt(Instant authorizationCodeIssuedAt) {
    this.authorizationCodeIssuedAt = authorizationCodeIssuedAt;
  }

  public Instant getAuthorizationCodeExpiresAt() {
    return this.authorizationCodeExpiresAt;
  }

  public void setAuthorizationCodeExpiresAt(Instant authorizationCodeExpiresAt) {
    this.authorizationCodeExpiresAt = authorizationCodeExpiresAt;
  }

  public String getAuthorizationCodeMetadata() {
    return this.authorizationCodeMetadata;
  }

  public void setAuthorizationCodeMetadata(String authorizationCodeMetadata) {
    this.authorizationCodeMetadata = authorizationCodeMetadata;
  }

  public String getAccessTokenValue() {
    return this.accessTokenValue;
  }

  public void setAccessTokenValue(String accessTokenValue) {
    this.accessTokenValue = accessTokenValue;
  }

  public Instant getAccessTokenIssuedAt() {
    return this.accessTokenIssuedAt;
  }

  public void setAccessTokenIssuedAt(Instant accessTokenIssuedAt) {
    this.accessTokenIssuedAt = accessTokenIssuedAt;
  }

  public Instant getAccessTokenExpiresAt() {
    return this.accessTokenExpiresAt;
  }

  public void setAccessTokenExpiresAt(Instant accessTokenExpiresAt) {
    this.accessTokenExpiresAt = accessTokenExpiresAt;
  }

  public String getAccessTokenMetadata() {
    return this.accessTokenMetadata;
  }

  public void setAccessTokenMetadata(String accessTokenMetadata) {
    this.accessTokenMetadata = accessTokenMetadata;
  }

  public String getAccessTokenType() {
    return this.accessTokenType;
  }

  public void setAccessTokenType(String accessTokenType) {
    this.accessTokenType = accessTokenType;
  }

  public String getAccessTokenScopes() {
    return this.accessTokenScopes;
  }

  public void setAccessTokenScopes(String accessTokenScopes) {
    this.accessTokenScopes = accessTokenScopes;
  }

  public String getRefreshTokenValue() {
    return this.refreshTokenValue;
  }

  public void setRefreshTokenValue(String refreshTokenValue) {
    this.refreshTokenValue = refreshTokenValue;
  }

  public Instant getRefreshTokenIssuedAt() {
    return this.refreshTokenIssuedAt;
  }

  public void setRefreshTokenIssuedAt(Instant refreshTokenIssuedAt) {
    this.refreshTokenIssuedAt = refreshTokenIssuedAt;
  }

  public Instant getRefreshTokenExpiresAt() {
    return this.refreshTokenExpiresAt;
  }

  public void setRefreshTokenExpiresAt(Instant refreshTokenExpiresAt) {
    this.refreshTokenExpiresAt = refreshTokenExpiresAt;
  }

  public String getRefreshTokenMetadata() {
    return this.refreshTokenMetadata;
  }

  public void setRefreshTokenMetadata(String refreshTokenMetadata) {
    this.refreshTokenMetadata = refreshTokenMetadata;
  }

  public String getOidcIdTokenValue() {
    return this.oidcIdTokenValue;
  }

  public void setOidcIdTokenValue(String oidcIdTokenValue) {
    this.oidcIdTokenValue = oidcIdTokenValue;
  }

  public Instant getOidcIdTokenIssuedAt() {
    return this.oidcIdTokenIssuedAt;
  }

  public void setOidcIdTokenIssuedAt(Instant oidcIdTokenIssuedAt) {
    this.oidcIdTokenIssuedAt = oidcIdTokenIssuedAt;
  }

  public Instant getOidcIdTokenExpiresAt() {
    return this.oidcIdTokenExpiresAt;
  }

  public void setOidcIdTokenExpiresAt(Instant oidcIdTokenExpiresAt) {
    this.oidcIdTokenExpiresAt = oidcIdTokenExpiresAt;
  }

  public String getOidcIdTokenMetadata() {
    return this.oidcIdTokenMetadata;
  }

  public void setOidcIdTokenMetadata(String oidcIdTokenMetadata) {
    this.oidcIdTokenMetadata = oidcIdTokenMetadata;
  }

  public String getOidcIdTokenClaims() {
    return this.oidcIdTokenClaims;
  }

  public void setOidcIdTokenClaims(String oidcIdTokenClaims) {
    this.oidcIdTokenClaims = oidcIdTokenClaims;
  }

  public String getUserCodeValue() {
    return this.userCodeValue;
  }

  public void setUserCodeValue(String userCodeValue) {
    this.userCodeValue = userCodeValue;
  }

  public Instant getUserCodeIssuedAt() {
    return this.userCodeIssuedAt;
  }

  public void setUserCodeIssuedAt(Instant userCodeIssuedAt) {
    this.userCodeIssuedAt = userCodeIssuedAt;
  }

  public Instant getUserCodeExpiresAt() {
    return this.userCodeExpiresAt;
  }

  public void setUserCodeExpiresAt(Instant userCodeExpiresAt) {
    this.userCodeExpiresAt = userCodeExpiresAt;
  }

  public String getUserCodeMetadata() {
    return this.userCodeMetadata;
  }

  public void setUserCodeMetadata(String userCodeMetadata) {
    this.userCodeMetadata = userCodeMetadata;
  }

  public String getDeviceCodeValue() {
    return this.deviceCodeValue;
  }

  public void setDeviceCodeValue(String deviceCodeValue) {
    this.deviceCodeValue = deviceCodeValue;
  }

  public Instant getDeviceCodeIssuedAt() {
    return this.deviceCodeIssuedAt;
  }

  public void setDeviceCodeIssuedAt(Instant deviceCodeIssuedAt) {
    this.deviceCodeIssuedAt = deviceCodeIssuedAt;
  }

  public Instant getDeviceCodeExpiresAt() {
    return this.deviceCodeExpiresAt;
  }

  public void setDeviceCodeExpiresAt(Instant deviceCodeExpiresAt) {
    this.deviceCodeExpiresAt = deviceCodeExpiresAt;
  }

  public String getDeviceCodeMetadata() {
    return this.deviceCodeMetadata;
  }

  public void setDeviceCodeMetadata(String deviceCodeMetadata) {
    this.deviceCodeMetadata = deviceCodeMetadata;
  }
}
