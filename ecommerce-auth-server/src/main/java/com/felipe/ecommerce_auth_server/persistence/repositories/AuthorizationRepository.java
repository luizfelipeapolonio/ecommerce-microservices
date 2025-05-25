package com.felipe.ecommerce_auth_server.persistence.repositories;

import com.felipe.ecommerce_auth_server.persistence.entities.AuthorizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthorizationRepository extends JpaRepository<AuthorizationEntity, String> {
  Optional<AuthorizationEntity> findByState(String state);
  Optional<AuthorizationEntity> findByAuthorizationCodeValue(String authorizationCode);
  Optional<AuthorizationEntity> findByAccessTokenValue(String accessToken);
  Optional<AuthorizationEntity> findByRefreshTokenValue(String refreshToken);
  Optional<AuthorizationEntity> findByOidcIdTokenValue(String idToken);
  Optional<AuthorizationEntity> findByUserCodeValue(String userCode);
  Optional<AuthorizationEntity> findByDeviceCodeValue(String deviceCode);

  @Query("SELECT a FROM AuthorizationEntity a WHERE a.state = :token" +
          " OR a.authorizationCodeValue = :token" +
          " OR a.accessTokenValue = :token" +
          " OR a.refreshTokenValue = :token" +
          " OR a.oidcIdTokenValue = :token" +
          " OR a.userCodeValue = :token" +
          " OR a.deviceCodeValue = :token")
  Optional<AuthorizationEntity> findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValueOrOidcIdTokenValueOrUserCodeValueOrDeviceCodeValue(@Param("token") String token);
}
