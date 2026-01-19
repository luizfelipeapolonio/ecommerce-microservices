package com.felipe.ecommerce_auth_server.config;

import com.felipe.ecommerce_auth_server.persistence.entities.UserEntity;
import com.felipe.ecommerce_auth_server.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class DataInitializer {

  @Value("${users.admin.login}")
  private String adminLogin;

  @Value("${users.admin.password}")
  private String adminPassword;
  private final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

  @Bean
  CommandLineRunner insertAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return args -> {
      Optional<UserEntity> existingUser = userRepository.findByEmail(this.adminLogin);
      if(existingUser.isPresent()) {
        this.logger.info("Data Initializer -> Admin user already exists");
        return;
      }

      UserEntity userEntity = new UserEntity();
      userEntity.setEmail(this.adminLogin);
      userEntity.setPassword(passwordEncoder.encode(this.adminPassword));
      userEntity.setRole("ADMIN");

      this.logger.info("Data Initializer -> Admin user created");
      userRepository.save(userEntity);
    };
  }
}
