package com.felipe.ecommerce_auth_server.services;

import com.felipe.ecommerce_auth_server.dtos.CreateUserDTO;
import com.felipe.ecommerce_auth_server.dtos.ResponseDTO;
import com.felipe.ecommerce_auth_server.enums.StatusResponse;
import com.felipe.ecommerce_auth_server.exceptions.EmailAlreadyExistsException;
import com.felipe.ecommerce_auth_server.persistence.entities.UserEntity;
import com.felipe.ecommerce_auth_server.persistence.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public ResponseDTO create(CreateUserDTO userDTO) {
    Optional<UserEntity> existingUser = this.userRepository.findByEmail(userDTO.email());
    if(existingUser.isPresent()) {
      throw new EmailAlreadyExistsException(existingUser.get().getEmail());
    }

    UserEntity user = new UserEntity();
    user.setEmail(userDTO.email());
    user.setPassword(this.passwordEncoder.encode(userDTO.password()));
    user.setRole("USER");

    this.userRepository.save(user);
    return new ResponseDTO(StatusResponse.SUCCESS, HttpStatus.CREATED, "Usuário criado com sucesso");
  }
}
